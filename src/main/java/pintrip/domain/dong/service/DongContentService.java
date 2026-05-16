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
import pintrip.global.error.BusinessException;
import pintrip.global.error.ErrorCode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DongContentService {

    private final DongRepository dongRepository;
    private final DongImageMappingRepository dongImageMappingRepository;
    private final ImageCardQuestRepository imageCardQuestRepository;

    public List<ImageCardResponse> getImageCards(Long dongId) {
        findActiveDong(dongId);

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
                        Collectors.mapping(ImageCardQuestResponse::new, Collectors.toList())
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

    private Dong findActiveDong(Long dongId) {
        return dongRepository.findById(dongId)
                .filter(Dong::isActive)
                .orElseThrow(() -> new BusinessException(ErrorCode.DONG_NOT_FOUND));
    }
}
