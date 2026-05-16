package pintrip.demo.domain.tripquest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import pintrip.demo.domain.session.entity.TripSession;
import java.util.UUID;

@Getter
@Entity
@Table(name = "trip_quests")
public class TripQuest {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private TripSession session;

    @Column(name = "place_description")
    private String placeDescription;

    @Column(name = "quest_description")
    private String questDescription;

    private String discovery;

    private String review;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    protected TripQuest() {}

    public static TripQuest create(TripSession session, String placeDescription, String questDescription) {
        TripQuest tq = new TripQuest();
        tq.id = UUID.randomUUID().toString();
        tq.session = session;
        tq.placeDescription = placeDescription;
        tq.questDescription = questDescription;
        tq.isCompleted = false;
        return tq;
    }

    public void update(String discovery, String review, Boolean isCompleted) {
        if (discovery != null) this.discovery = discovery;
        if (review != null) this.review = review;
        if (isCompleted != null) this.isCompleted = isCompleted;
    }
}
