package pintrip.domain.dong.service;

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
import pintrip.domain.session.repository.TripSessionQuestReviewRepository;
import pintrip.domain.session.service.TripSessionStatusResolver;
import pintrip.global.error.BusinessException;
import pintrip.global.error.ErrorCode;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DongContentService {

    private final DongRepository dongRepository;
    private final DongImageMappingRepository dongImageMappingRepository;
    private final ImageCardQuestRepository imageCardQuestRepository;
    private final TripSessionQuestReviewRepository reviewRepository;
    private final TripSessionStatusResolver sessionStatusResolver;

    public List<ImageCardResponse> getImageCards(Long dongId) {
        return getImageCards(dongId, null);
    }

    @Transactional
    public List<ImageCardResponse> getImageCards(Long dongId, String sessionId) {
        findActiveDong(dongId);

        Set<Long> completedQuestIds = resolveCompletedQuestIds(dongId, sessionId);

        List<DongImageMapping> mappings = dongImageMappingRepository.findAllByDongIdOrderByIdAsc(dongId);
        if (mappings.isEmpty()) {
            throw new BusinessException(ErrorCode.PLACE_NOT_FOUND);
        }

        List<Long> imageCardIds = mappings.stream()
                .map(DongImageMapping::getId)
                .toList();

        Map<Long, List<ImageCardQuestResponse>> questMap = imageCardQuestRepository
                .findAllByImageCardIdInOrderByImageCardIdAscQuestOrderAsc(imageCardIds)
                .stream()
                .collect(Collectors.groupingBy(
                        q -> q.getImageCard().getId(),
                        Collectors.mapping(
                                q -> new ImageCardQuestResponse(q, completedQuestIds.contains(q.getId())),
                                Collectors.toList()
                        )
                ));

        return mappings.stream()
                .map(mapping -> {
                    List<ImageCardQuestResponse> quests = questMap.get(mapping.getId());
                    if (quests == null || quests.size() != 3) {
                        throw new BusinessException(ErrorCode.QUEST_NOT_FOUND);
                    }
                    return new ImageCardResponse(mapping, quests);
                })
                .toList();
    }

    private Set<Long> resolveCompletedQuestIds(Long dongId, String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return Set.of();
        }

        var session = sessionStatusResolver.resolveForRead(sessionId);
        if (!session.getDong().getId().equals(dongId)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        return reviewRepository.findAllBySession_IdOrderByImageCard_IdAscQuest_QuestOrderAsc(sessionId)
                .stream()
                .map(review -> review.getQuest().getId())
                .collect(Collectors.toCollection(HashSet::new));
    }

    private Dong findActiveDong(Long dongId) {
        return dongRepository.findById(dongId)
                .filter(Dong::isActive)
                .orElseThrow(() -> new BusinessException(ErrorCode.DONG_NOT_FOUND));
    }
}
