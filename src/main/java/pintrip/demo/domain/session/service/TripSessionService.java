package pintrip.demo.domain.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pintrip.demo.domain.dong.entity.Dong;
import pintrip.demo.domain.dong.repository.DongRepository;
import pintrip.demo.domain.place.dto.PlaceRandomResponse;
import pintrip.demo.domain.place.entity.Place;
import pintrip.demo.domain.place.service.PlaceService;
import pintrip.demo.domain.quest.dto.QuestRandomResponse;
import pintrip.demo.domain.quest.entity.Quest;
import pintrip.demo.domain.quest.service.QuestService;
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
    private final PlaceService placeService;
    private final QuestService questService;

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
        return new TripSessionResponse(findSession(sessionId));
    }

    public PlaceRandomResponse assignRandomPlace(String sessionId) {
        TripSession session = findSession(sessionId);
        Place place = placeService.getRandomPlaceByDongId(session.getDong().getId());
        session.assignPlace(place);
        return new PlaceRandomResponse(place);
    }

    public QuestRandomResponse assignRandomQuest(String sessionId) {
        TripSession session = findSession(sessionId);
        if (session.getSelectedPlace() == null) {
            throw new BusinessException(ErrorCode.PLACE_NOT_FOUND);
        }
        Quest quest = questService.getRandomQuestByPlaceId(session.getSelectedPlace().getId());
        session.assignQuest(quest);
        return new QuestRandomResponse(quest);
    }

    private TripSession findSession(String sessionId) {
        return tripSessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SESSION_NOT_FOUND));
    }
}
