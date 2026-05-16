package pintrip.domain.session.entity;

import jakarta.persistence.*;
import lombok.Getter;
import pintrip.domain.dong.entity.Dong;
import pintrip.domain.image.entity.DongImageMapping;
import pintrip.domain.session.TripSessionPolicy;

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
    @JoinColumn(name = "selected_image_card_id", nullable = false)
    private DongImageMapping selectedImageCard;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    protected TripSession() {}

    public static TripSession create(Dong dong, DongImageMapping selectedImageCard) {
        TripSession session = new TripSession();
        session.id = UUID.randomUUID().toString();
        session.dong = dong;
        session.selectedImageCard = selectedImageCard;
        session.status = "ACTIVE";
        session.createdAt = LocalDateTime.now();
        session.updatedAt = LocalDateTime.now();
        session.expiredAt = session.createdAt.plusDays(TripSessionPolicy.EXPIRE_DAYS);
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

    /** TTL 경과 또는 '새 여행' 등으로 세션을 종료한다. 이후 후기 작성 불가. */
    public void expire() {
        this.status = "EXPIRED";
        this.updatedAt = LocalDateTime.now();
        if (LocalDateTime.now().isBefore(expiredAt)) {
            this.expiredAt = LocalDateTime.now();
        }
    }

    public boolean isExpired() {
        return "EXPIRED".equals(this.status);
    }

    public boolean isUsable() {
        return isActive() && !isExpiredByTime();
    }
}
