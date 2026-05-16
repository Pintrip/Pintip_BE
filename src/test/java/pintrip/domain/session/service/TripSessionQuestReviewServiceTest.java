package pintrip.domain.session.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pintrip.domain.dong.entity.Dong;
import pintrip.domain.image.entity.DongImageMapping;
import pintrip.domain.image.entity.ImageCardQuest;
import pintrip.domain.image.repository.DongImageMappingRepository;
import pintrip.domain.image.repository.ImageCardQuestRepository;
import pintrip.domain.session.dto.QuestReviewUpsertRequest;
import pintrip.domain.session.entity.TripSession;
import pintrip.domain.session.entity.TripSessionQuestReview;
import pintrip.domain.session.repository.TripSessionQuestReviewRepository;
import pintrip.global.error.BusinessException;
import pintrip.global.error.ErrorCode;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripSessionQuestReviewServiceTest {

    @Mock
    private TripSessionQuestReviewRepository reviewRepository;
    @Mock
    private TripSessionStatusResolver sessionStatusResolver;
    @Mock
    private DongImageMappingRepository dongImageMappingRepository;
    @Mock
    private ImageCardQuestRepository imageCardQuestRepository;

    @InjectMocks
    private TripSessionQuestReviewService questReviewService;

    @Test
    void upsertReview_createsNewReview() {
        TripSession session = activeSession("session-1", 1L);
        DongImageMapping imageCard = imageCard(1L, 1L);
        ImageCardQuest quest = quest(10L, imageCard);
        QuestReviewUpsertRequest request = request(1L);

        when(sessionStatusResolver.requireWritable("session-1")).thenReturn(session);
        when(dongImageMappingRepository.findById(1L)).thenReturn(Optional.of(imageCard));
        when(imageCardQuestRepository.findByIdAndImageCard_Id(10L, 1L)).thenReturn(Optional.of(quest));
        when(reviewRepository.findBySession_IdAndQuest_Id("session-1", 10L)).thenReturn(Optional.empty());
        when(reviewRepository.save(any(TripSessionQuestReview.class))).thenAnswer(invocation -> {
            TripSessionQuestReview review = invocation.getArgument(0);
            return review;
        });
        when(sessionStatusResolver.resolve(session)).thenReturn(session);

        var response = questReviewService.upsertReview("session-1", 10L, request);

        assertThat(response.getQuestId()).isEqualTo(10L);
        assertThat(response.getDiscoveredNote()).isEqualTo("발견");
        assertThat(response.isCompleted()).isTrue();
        assertThat(response.isWritten()).isTrue();
        verify(reviewRepository).save(any(TripSessionQuestReview.class));
    }

    @Test
    void upsertReview_updatesExistingReview() {
        TripSession session = activeSession("session-1", 1L);
        DongImageMapping imageCard = imageCard(1L, 1L);
        ImageCardQuest quest = quest(10L, imageCard);
        TripSessionQuestReview existing = TripSessionQuestReview.create(
                session, imageCard, quest, "old", "old review", List.of("낯설었다")
        );
        QuestReviewUpsertRequest request = request(1L);

        when(sessionStatusResolver.requireWritable("session-1")).thenReturn(session);
        when(dongImageMappingRepository.findById(1L)).thenReturn(Optional.of(imageCard));
        when(imageCardQuestRepository.findByIdAndImageCard_Id(10L, 1L)).thenReturn(Optional.of(quest));
        when(reviewRepository.findBySession_IdAndQuest_Id("session-1", 10L)).thenReturn(Optional.of(existing));
        when(reviewRepository.save(existing)).thenReturn(existing);
        when(sessionStatusResolver.resolve(session)).thenReturn(session);

        var response = questReviewService.upsertReview("session-1", 10L, request);

        assertThat(response.getDiscoveredNote()).isEqualTo("발견");
        assertThat(response.getReviewText()).isEqualTo("후기");
    }

    @Test
    void upsertReview_throwsWhenSessionNotWritable() {
        when(sessionStatusResolver.requireWritable("session-1"))
                .thenThrow(new BusinessException(ErrorCode.SESSION_EXPIRED));

        assertThatThrownBy(() -> questReviewService.upsertReview("session-1", 10L, request(1L)))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.SESSION_EXPIRED);
    }

    @Test
    void upsertReview_throwsWhenImageCardNotInSessionDong() {
        TripSession session = activeSession("session-1", 1L);
        DongImageMapping imageCard = imageCard(2L, 99L);

        when(sessionStatusResolver.requireWritable("session-1")).thenReturn(session);
        when(dongImageMappingRepository.findById(2L)).thenReturn(Optional.of(imageCard));

        assertThatThrownBy(() -> questReviewService.upsertReview("session-1", 10L, request(2L)))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_REQUEST);
    }

    @Test
    void upsertReview_throwsWhenQuestNotLinkedToCard() {
        TripSession session = activeSession("session-1", 1L);
        DongImageMapping imageCard = imageCard(1L, 1L);

        when(sessionStatusResolver.requireWritable("session-1")).thenReturn(session);
        when(dongImageMappingRepository.findById(1L)).thenReturn(Optional.of(imageCard));
        when(imageCardQuestRepository.findByIdAndImageCard_Id(10L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> questReviewService.upsertReview("session-1", 10L, request(1L)))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.QUEST_NOT_FOUND);
    }

    private TripSession activeSession(String id, Long dongId) {
        Dong dong = new Dong();
        setField(dong, "id", dongId);
        TripSession session = TripSession.create(dong);
        setField(session, "id", id);
        return session;
    }

    private DongImageMapping imageCard(Long cardId, Long dongId) {
        Dong dong = new Dong();
        setField(dong, "id", dongId);
        DongImageMapping card = new DongImageMapping();
        setField(card, "id", cardId);
        setField(card, "dong", dong);
        return card;
    }

    private ImageCardQuest quest(Long questId, DongImageMapping card) {
        ImageCardQuest quest = new ImageCardQuest();
        setField(quest, "id", questId);
        setField(quest, "imageCard", card);
        setField(quest, "questTitle", "퀘스트");
        setField(quest, "questDescription", "설명");
        setField(quest, "questOrder", 1);
        return quest;
    }

    private QuestReviewUpsertRequest request(Long imageCardId) {
        QuestReviewUpsertRequest request = new QuestReviewUpsertRequest();
        setField(request, "imageCardId", imageCardId);
        setField(request, "discoveredNote", "발견");
        setField(request, "reviewText", "후기");
        setField(request, "moodTags", List.of("조용했다"));
        return request;
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
