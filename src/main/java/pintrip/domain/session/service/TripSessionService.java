package pintrip.domain.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pintrip.domain.dong.entity.Dong;
import pintrip.domain.dong.repository.DongRepository;
import pintrip.domain.session.dto.TripSessionCreateRequest;
import pintrip.domain.session.dto.TripSessionCreateResponse;
import pintrip.domain.session.dto.TripSessionResponse;
import pintrip.domain.session.entity.TripSession;
import pintrip.domain.session.repository.TripSessionRepository;
import pintrip.global.error.BusinessException;
import pintrip.global.error.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional
public class TripSessionService {

    private final TripSessionRepository tripSessionRepository;
    private final DongRepository dongRepository;

    public TripSessionCreateResponse createSession(TripSessionCreateRequest request) {
        Dong dong = dongRepository.findById(request.getDongId())
                .filter(Dong::isActive)
                .orElseThrow(() -> new BusinessException(ErrorCode.DONG_NOT_FOUND));
        TripSession session = TripSession.create(dong);
        tripSessionRepository.save(session);
        return new TripSessionCreateResponse(session);
    }

    @Transactional(readOnly = true)
    public TripSessionResponse getSession(String sessionId) {
        TripSession session = findActiveSession(sessionId);
        return new TripSessionResponse(session);
    }

    @Transactional
    public void completeSession(String sessionId) {
        TripSession session = findActiveSession(sessionId);
        session.complete();
    }

    private TripSession findActiveSession(String sessionId) {
        TripSession session = tripSessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SESSION_NOT_FOUND));

        if (!session.isActive() || session.isExpiredByTime()) {
            throw new BusinessException(ErrorCode.SESSION_EXPIRED);
        }

        return session;
    }
}
