package pintrip.demo.domain.session.entity;

import jakarta.persistence.*;
import lombok.Getter;
import pintrip.demo.domain.dong.entity.Dong;
import pintrip.demo.domain.place.entity.Place;
import pintrip.demo.domain.quest.entity.Quest;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "trip_sessions")
public class TripSession {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_id", nullable = false)
    private Dong dong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_place_id")
    private Place selectedPlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_quest_id")
    private Quest selectedQuest;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected TripSession() {}

    public static TripSession create(Dong dong) {
        TripSession session = new TripSession();
        session.id = UUID.randomUUID().toString();
        session.dong = dong;
        session.status = "ACTIVE";
        session.createdAt = LocalDateTime.now();
        session.updatedAt = LocalDateTime.now();
        return session;
    }

    public void assignPlace(Place place) {
        this.selectedPlace = place;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignQuest(Quest quest) {
        this.selectedQuest = quest;
        this.updatedAt = LocalDateTime.now();
    }
}
