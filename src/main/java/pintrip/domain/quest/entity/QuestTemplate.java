package pintrip.domain.quest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "quest_templates")
public class QuestTemplate {
    @Id
    @Column(length = 10)
    private String code;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;
}
