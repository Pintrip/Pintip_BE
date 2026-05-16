package pintrip.domain.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pintrip.domain.session.TripSessionPolicy;
import pintrip.domain.session.entity.TripSession;
import pintrip.domain.session.repository.TripSessionQuestReviewRepository;
import pintrip.domain.session.repository.TripSessionRepository;
import pintrip.global.error.BusinessException;
import pintrip.global.error.ErrorCode;

@Component
@RequiredArgsConstructor
public class TripSessionStatusResolver {

    private final TripSessionRepository tripSessionRepository;
    private final TripSessionQuestReviewRepository reviewRepository;

    public TripSession findSession(String sessionId) {
        return tripSessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SESSION_NOT_FOUND));
    }

    public TripSession resolve(TripSession session) {
        if (!session.isActive()) {
            return session;
        }

        boolean expired = session.isExpiredByTime();
        boolean allQuestsReviewed = reviewRepository.countBySession_Id(session.getId())
                >= TripSessionPolicy.REQUIRED_QUEST_REVIEWS;

        if (expired || allQuestsReviewed) {
            session.complete();
            return tripSessionRepository.save(session);
        }

        return session;
    }

    public TripSession resolveForRead(String sessionId) {
        return resolve(findSession(sessionId));
    }

    public TripSession requireWritable(String sessionId) {
        TripSession session = resolve(findSession(sessionId));
        if (!session.isActive()) {
            throw new BusinessException(ErrorCode.SESSION_EXPIRED);
        }
        return session;
    }
}
