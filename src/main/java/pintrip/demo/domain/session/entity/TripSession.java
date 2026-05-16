package pintrip.demo.domain.session.entity;

import jakarta.persistence.*;
import lombok.Getter;
import pintrip.demo.domain.dong.entity.Dong;
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

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    protected TripSession() {}

    public static TripSession create(Dong dong) {
        TripSession session = new TripSession();
        session.id = UUID.randomUUID().toString();
        session.dong = dong;
        session.status = "ACTIVE";
        session.createdAt = LocalDateTime.now();
        session.updatedAt = LocalDateTime.now();
        session.expiredAt = session.createdAt.plusDays(2);
        return session;
    }

    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    public boolean isExpiredByTime() {
        return LocalDateTime.now().isAfter(expiredAt);
    }

    public void complete() {
        this.status = "COMPLETED";
        this.updatedAt = LocalDateTime.now();
    }
}
