package pintrip.demo.domain.quest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import pintrip.demo.domain.place.entity.Place;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "quests")
public class Quest {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String difficulty;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
