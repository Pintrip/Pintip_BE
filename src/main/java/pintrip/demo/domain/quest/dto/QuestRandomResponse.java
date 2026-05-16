package pintrip.demo.domain.quest.dto;

import lombok.Getter;
import pintrip.demo.domain.quest.entity.Quest;

@Getter
public class QuestRandomResponse {
    private final QuestResponse quest;

    public QuestRandomResponse(Quest quest) {
        this.quest = new QuestResponse(quest);
    }
}
