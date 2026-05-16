package pintrip.demo.domain.quest.dto;

import lombok.Getter;
import pintrip.demo.domain.quest.entity.Quest;

@Getter
public class QuestResponse {
    private final Long id;
    private final String title;
    private final String description;

    public QuestResponse(Quest quest) {
        this.id = quest.getId();
        this.title = quest.getTitle();
        this.description = quest.getDescription();
    }
}
