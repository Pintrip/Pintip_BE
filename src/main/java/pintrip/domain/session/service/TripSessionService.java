package pintrip.domain.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pintrip.domain.dong.entity.Dong;
import pintrip.domain.dong.repository.DongRepository;
import pintrip.domain.image.entity.DongImageMapping;
import pintrip.domain.image.entity.ImageCardQuest;
import pintrip.domain.image.repository.DongImageMappingRepository;
import pintrip.domain.image.repository.ImageCardQuestRepository;
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

import java.util.List;

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
        ImageCardQuest quest = imageCardQuestRepository.findByIdAndImageCard_Id(request.getQuestId(), request.getImageCardId())
                .orElseThrow(() -> new BusinessException(ErrorCode.QUEST_NOT_FOUND));

        TripSession session = TripSession.create(dong, imageCard, quest);
        tripSessionRepository.save(session);
        return new TripSessionCreateResponse(session);
    }

    @Transactional(readOnly = true)
    public TripSessionResponse getSession(String sessionId) {
        TripSession session = sessionStatusResolver.resolveForRead(sessionId);
        List<QuestReviewResponse> reviews = reviewRepository
                .findAllBySession_IdOrderByImageCard_IdAscQuest_QuestOrderAsc(sessionId)
                .stream()
                .map(QuestReviewResponse::new)
                .toList();
        return new TripSessionResponse(session, reviews);
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
}
