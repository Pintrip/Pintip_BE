package pintrip.domain.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pintrip.domain.dong.entity.Dong;
import pintrip.domain.dong.repository.DongRepository;
import pintrip.domain.image.entity.DongImageMapping;
import pintrip.domain.image.repository.DongImageMappingRepository;
import pintrip.domain.image.repository.ImageCardQuestRepository;
import pintrip.domain.session.dto.ImageCardQuestResponse;
import pintrip.domain.session.dto.ImageCardResponse;
import pintrip.domain.session.dto.QuestReviewResponse;
import pintrip.domain.session.dto.TripSessionCreateRequest;
import pintrip.domain.session.dto.TripSessionCreateResponse;
import pintrip.domain.session.dto.TripSessionExpiredResponse;
import pintrip.domain.session.dto.TripSessionResponse;
import pintrip.domain.session.entity.TripSession;
import pintrip.domain.session.repository.TripSessionQuestReviewRepository;
import pintrip.domain.session.repository.TripSessionRepository;
import pintrip.global.error.BusinessException;
import pintrip.global.error.ErrorCode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TripSessionService {

    private final TripSessionRepository tripSessionRepository;
    private final DongRepository dongRepository;
    private final DongImageMappingRepository dongImageMappingRepository;
    private final ImageCardQuestRepository imageCardQuestRepository;
    private final TripSessionQuestReviewRepository reviewRepository;
    private final TripSessionStatusResolver sessionStatusResolver;

    public TripSessionCreateResponse createSession(TripSessionCreateRequest request) {
        Dong dong = dongRepository.findById(request.getDongId())
                .filter(Dong::isActive)
                .orElseThrow(() -> new BusinessException(ErrorCode.DONG_NOT_FOUND));
        DongImageMapping imageCard = dongImageMappingRepository.findById(request.getImageCardId())
                .filter(card -> card.getDong().getId().equals(dong.getId()))
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));

        TripSession session = TripSession.create(dong, imageCard);
        tripSessionRepository.save(session);

        ImageCardResponse selectedImageCard = buildSelectedImageCard(session, Set.of());
        return new TripSessionCreateResponse(session, selectedImageCard);
    }

    @Transactional(readOnly = true)
    public TripSessionResponse getSession(String sessionId) {
        TripSession session = sessionStatusResolver.resolveForRead(sessionId);
        Set<Long> completedQuestIds = reviewRepository
                .findAllBySession_IdOrderByImageCard_IdAscQuest_QuestOrderAsc(sessionId)
                .stream()
                .map(review -> review.getQuest().getId())
                .collect(Collectors.toCollection(HashSet::new));
        List<QuestReviewResponse> reviews = reviewRepository
                .findAllBySession_IdOrderByImageCard_IdAscQuest_QuestOrderAsc(sessionId)
                .stream()
                .map(QuestReviewResponse::new)
                .toList();
        ImageCardResponse selectedImageCard = buildSelectedImageCard(session, completedQuestIds);
        return new TripSessionResponse(session, selectedImageCard, reviews);
    }

    @Transactional(readOnly = true)
    public TripSessionExpiredResponse getExpiredStatus(String sessionId) {
        TripSession session = sessionStatusResolver.resolveForRead(sessionId);
        return new TripSessionExpiredResponse(session);
    }

    public TripSessionExpiredResponse expireSession(String sessionId) {
        TripSession session = sessionStatusResolver.findSession(sessionId);
        if (session.isActive()) {
            session.expire();
            tripSessionRepository.save(session);
        }
        return new TripSessionExpiredResponse(session);
    }

    public void completeSession(String sessionId) {
        TripSession session = sessionStatusResolver.findSession(sessionId);
        if (session.isActive()) {
            session.complete();
            tripSessionRepository.save(session);
        }
    }

    private ImageCardResponse buildSelectedImageCard(TripSession session, Set<Long> completedQuestIds) {
        DongImageMapping mapping = session.getSelectedImageCard();
        List<ImageCardQuestResponse> quests = imageCardQuestRepository
                .findAllByImageCardIdInOrderByImageCardIdAscQuestOrderAsc(List.of(mapping.getId()))
                .stream()
                .map(quest -> new ImageCardQuestResponse(quest, completedQuestIds.contains(quest.getId())))
                .toList();
        if (quests.size() != 3) {
            throw new BusinessException(ErrorCode.QUEST_NOT_FOUND);
        }
        return new ImageCardResponse(mapping, quests);
    }
}
