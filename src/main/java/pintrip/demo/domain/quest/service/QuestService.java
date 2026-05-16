package pintrip.demo.domain.quest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pintrip.demo.domain.quest.entity.Quest;
import pintrip.demo.domain.quest.repository.QuestRepository;
import pintrip.demo.global.error.BusinessException;
import pintrip.demo.global.error.ErrorCode;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestService {

    private final QuestRepository questRepository;

    public Quest getRandomQuestByPlaceId(Long placeId) {
        List<Quest> quests = questRepository.findAllByPlaceId(placeId);
        if (quests.isEmpty()) {
            throw new BusinessException(ErrorCode.QUEST_NOT_FOUND);
        }
        return quests.get(ThreadLocalRandom.current().nextInt(quests.size()));
    }
}
