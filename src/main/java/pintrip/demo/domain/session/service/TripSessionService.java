package pintrip.demo.domain.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pintrip.demo.domain.dong.entity.Dong;
import pintrip.demo.domain.dong.repository.DongRepository;
import pintrip.demo.domain.session.dto.TripSessionCreateRequest;
import pintrip.demo.domain.session.dto.TripSessionCreateResponse;
import pintrip.demo.domain.session.dto.TripSessionResponse;
import pintrip.demo.domain.session.entity.TripSession;
import pintrip.demo.domain.session.repository.TripSessionRepository;
import pintrip.demo.global.error.BusinessException;
import pintrip.demo.global.error.ErrorCode;

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
        return new TripSessionResponse(findActiveSession(sessionId));
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
