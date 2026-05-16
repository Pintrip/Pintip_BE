package pintrip.demo.domain.place.entity;

import jakarta.persistence.*;
import lombok.Getter;
import pintrip.demo.domain.dong.entity.Dong;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "places")
public class Place {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_id", nullable = false)
    private Dong dong;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    private String description;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String mapKeyword;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
