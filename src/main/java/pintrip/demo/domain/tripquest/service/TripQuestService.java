package pintrip.demo.domain.tripquest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pintrip.demo.domain.session.entity.TripSession;
import pintrip.demo.domain.session.repository.TripSessionRepository;
import pintrip.demo.domain.tripquest.dto.TripQuestCreateRequest;
import pintrip.demo.domain.tripquest.dto.TripQuestCreateResponse;
import pintrip.demo.domain.tripquest.dto.TripQuestResponse;
import pintrip.demo.domain.tripquest.dto.TripQuestUpdateRequest;
import pintrip.demo.domain.tripquest.entity.TripQuest;
import pintrip.demo.domain.tripquest.repository.TripQuestRepository;
import pintrip.demo.global.error.BusinessException;
import pintrip.demo.global.error.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TripQuestService {

    private final TripQuestRepository tripQuestRepository;
    private final TripSessionRepository tripSessionRepository;

    public TripQuestCreateResponse createTripQuest(String sessionId, TripQuestCreateRequest request) {
        TripSession session = tripSessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SESSION_NOT_FOUND));
        TripQuest tripQuest = TripQuest.create(session, request.getPlaceDescription(), request.getQuestDescription());
        tripQuestRepository.save(tripQuest);
        return new TripQuestCreateResponse(tripQuest);
    }

    @Transactional(readOnly = true)
    public List<TripQuestResponse> getTripQuests(String sessionId) {
        if (!tripSessionRepository.existsById(sessionId)) {
            throw new BusinessException(ErrorCode.SESSION_NOT_FOUND);
        }
        return tripQuestRepository.findBySessionId(sessionId)
                .stream()
                .map(TripQuestResponse::new)
                .collect(Collectors.toList());
    }

    public TripQuestResponse updateTripQuest(String sessionId, String questId, TripQuestUpdateRequest request) {
        TripQuest tripQuest = tripQuestRepository.findById(questId)
                .filter(tq -> tq.getSession().getId().equals(sessionId))
                .orElseThrow(() -> new BusinessException(ErrorCode.TRIP_QUEST_NOT_FOUND));
        tripQuest.update(request.getDiscovery(), request.getReview(), request.getIsCompleted());
        return new TripQuestResponse(tripQuest);
    }

    public void deleteTripQuest(String sessionId, String questId) {
        TripQuest tripQuest = tripQuestRepository.findById(questId)
                .filter(tq -> tq.getSession().getId().equals(sessionId))
                .orElseThrow(() -> new BusinessException(ErrorCode.TRIP_QUEST_NOT_FOUND));
        tripQuestRepository.delete(tripQuest);
    }
}
