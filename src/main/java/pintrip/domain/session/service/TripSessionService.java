package pintrip.domain.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pintrip.domain.dong.entity.Dong;
import pintrip.domain.dong.repository.DongRepository;
import pintrip.domain.image.entity.DongImageMapping;
import pintrip.domain.image.repository.DongImageMappingRepository;
import pintrip.domain.quest.dto.QuestTemplateResponse;
import pintrip.domain.quest.entity.QuestTemplate;
import pintrip.domain.quest.repository.QuestTemplateRepository;
import pintrip.domain.session.dto.ImageCardResponse;
import pintrip.domain.session.dto.TripSessionCreateRequest;
import pintrip.domain.session.dto.TripSessionResponse;
import pintrip.domain.session.entity.TripSession;
import pintrip.domain.session.repository.TripSessionRepository;
import pintrip.global.error.BusinessException;
import pintrip.global.error.ErrorCode;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TripSessionService {

    private final TripSessionRepository tripSessionRepository;
    private final DongRepository dongRepository;
    private final DongImageMappingRepository dongImageMappingRepository;
    private final QuestTemplateRepository questTemplateRepository;

    public TripSessionResponse createSession(TripSessionCreateRequest request) {
        Dong dong = dongRepository.findById(request.getDongId())
                .filter(Dong::isActive)
                .orElseThrow(() -> new BusinessException(ErrorCode.DONG_NOT_FOUND));
        TripSession session = TripSession.create(dong);
        tripSessionRepository.save(session);
        List<ImageCardResponse> imageCards = getImageCardsByDong(dong.getId());
        List<QuestTemplateResponse> quests = getQuestListByDong(dong.getId());
        return new TripSessionResponse(session, imageCards, quests);
    }

    @Transactional(readOnly = true)
    public TripSessionResponse getSession(String sessionId) {
        TripSession session = findActiveSession(sessionId);
        List<ImageCardResponse> imageCards = getImageCardsByDong(session.getDong().getId());
        List<QuestTemplateResponse> quests = getQuestListByDong(session.getDong().getId());
        return new TripSessionResponse(session, imageCards, quests);
    }

    @Transactional
    public void completeSession(String sessionId) {
        TripSession session = findActiveSession(sessionId);
        session.complete();
    }

    @Transactional(readOnly = true)
    public List<ImageCardResponse> getImageCards(String sessionId) {
        TripSession session = findActiveSession(sessionId);
        return getImageCardsByDong(session.getDong().getId());
    }

    @Transactional(readOnly = true)
    public List<QuestTemplateResponse> getQuestList(String sessionId) {
        TripSession session = findActiveSession(sessionId);
        return getQuestListByDong(session.getDong().getId());
    }

    private TripSession findActiveSession(String sessionId) {
        TripSession session = tripSessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SESSION_NOT_FOUND));

        if (!session.isActive() || session.isExpiredByTime()) {
            throw new BusinessException(ErrorCode.SESSION_EXPIRED);
        }

        return session;
    }

    private List<ImageCardResponse> getImageCardsByDong(Long dongId) {
        List<DongImageMapping> mappings = dongImageMappingRepository.findAllByDongId(dongId)
                .stream()
                .sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                .toList();
        if (mappings.isEmpty()) {
            throw new BusinessException(ErrorCode.PLACE_NOT_FOUND);
        }
        return mappings.stream()
                .map(this::buildImageCardResponse)
                .toList();
    }

    private List<QuestTemplateResponse> getQuestListByDong(Long dongId) {
        List<DongImageMapping> mappings = dongImageMappingRepository.findAllByDongId(dongId)
                .stream()
                .sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                .toList();
        if (mappings.isEmpty()) {
            throw new BusinessException(ErrorCode.PLACE_NOT_FOUND);
        }
        LinkedHashSet<String> orderedQuestCodes = new LinkedHashSet<>();
        for (DongImageMapping mapping : mappings) {
            orderedQuestCodes.addAll(mapping.questCodes());
        }
        List<String> codes = new ArrayList<>(orderedQuestCodes);
        Map<String, QuestTemplate> questMap = questTemplateRepository.findAllById(codes)
                .stream()
                .collect(Collectors.toMap(QuestTemplate::getCode, Function.identity()));
        return codes.stream()
                .map(code -> {
                    QuestTemplate template = questMap.get(code);
                    if (template == null) {
                        throw new BusinessException(ErrorCode.QUEST_NOT_FOUND);
                    }
                    return new QuestTemplateResponse(template);
                })
                .toList();
    }

    private ImageCardResponse buildImageCardResponse(DongImageMapping mapping) {
        List<String> questCodes = mapping.questCodes();
        Map<String, QuestTemplate> questMap = questTemplateRepository.findAllById(questCodes)
                .stream()
                .collect(Collectors.toMap(QuestTemplate::getCode, Function.identity()));
        List<QuestTemplateResponse> quests = questCodes.stream()
                .map(code -> {
                    QuestTemplate template = questMap.get(code);
                    if (template == null) {
                        throw new BusinessException(ErrorCode.QUEST_NOT_FOUND);
                    }
                    return new QuestTemplateResponse(template);
                })
                .toList();
        return new ImageCardResponse(mapping, quests);
    }
}
