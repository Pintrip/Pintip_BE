package pintrip.domain.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pintrip.domain.image.entity.DongImageMapping;
import pintrip.domain.image.entity.ImageCardQuest;
import pintrip.domain.image.repository.DongImageMappingRepository;
import pintrip.domain.image.repository.ImageCardQuestRepository;
import pintrip.domain.session.dto.QuestReviewResponse;
import pintrip.domain.session.dto.QuestReviewUpsertRequest;
import pintrip.domain.session.entity.TripSession;
import pintrip.domain.session.entity.TripSessionQuestReview;
import pintrip.domain.session.repository.TripSessionQuestReviewRepository;
import pintrip.global.error.BusinessException;
import pintrip.global.error.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TripSessionQuestReviewService {

    private final TripSessionQuestReviewRepository reviewRepository;
    private final TripSessionStatusResolver sessionStatusResolver;
    private final DongImageMappingRepository dongImageMappingRepository;
    private final ImageCardQuestRepository imageCardQuestRepository;

    public QuestReviewResponse saveReview(String sessionId, Long questId, QuestReviewUpsertRequest request) {
        TripSession session = sessionStatusResolver.requireWritable(sessionId);
        validateSelectedImageCard(session, request.getImageCardId());
        DongImageMapping imageCard = validateImageCard(session, request.getImageCardId());
        ImageCardQuest quest = validateQuest(request.getImageCardId(), questId);

        if (reviewRepository.findBySession_IdAndQuest_Id(sessionId, questId).isPresent()) {
            throw new BusinessException(ErrorCode.QUEST_REVIEW_ALREADY_EXISTS);
        }

        TripSessionQuestReview review = TripSessionQuestReview.create(
                session,
                imageCard,
                quest,
                request.getDiscoveredNote(),
                request.getReviewText()
        );

        QuestReviewResponse response = new QuestReviewResponse(reviewRepository.save(review));
        sessionStatusResolver.resolve(session);
        return response;
    }

    public List<QuestReviewResponse> getReviews(String sessionId) {
        sessionStatusResolver.resolveForRead(sessionId);
        return reviewRepository.findAllBySession_IdOrderByImageCard_IdAscQuest_QuestOrderAsc(sessionId)
                .stream()
                .map(QuestReviewResponse::new)
                .toList();
    }

    private DongImageMapping validateImageCard(TripSession session, Long imageCardId) {
        return dongImageMappingRepository.findById(imageCardId)
                .filter(card -> card.getDong().getId().equals(session.getDong().getId()))
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));
    }

    private ImageCardQuest validateQuest(Long imageCardId, Long questId) {
        return imageCardQuestRepository.findByIdAndImageCard_Id(questId, imageCardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUEST_NOT_FOUND));
    }

    private void validateSelectedImageCard(TripSession session, Long imageCardId) {
        if (!session.getSelectedImageCard().getId().equals(imageCardId)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
    }
}
