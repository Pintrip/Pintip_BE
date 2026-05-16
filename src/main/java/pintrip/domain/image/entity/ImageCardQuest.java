package pintrip.domain.image.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "image_card_quests")
public class ImageCardQuest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_card_id", nullable = false)
    private DongImageMapping imageCard;

    @Column(name = "quest_title", nullable = false)
    private String questTitle;

    @Column(name = "quest_description", nullable = false, length = 1000)
    private String questDescription;

    @Column(name = "quest_order", nullable = false)
    private int questOrder;
}
