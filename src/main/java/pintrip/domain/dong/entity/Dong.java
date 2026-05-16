package pintrip.domain.dong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "dongs")
public class Dong {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

//    @Column(nullable = false)
//    private String city;
//
//    @Column(nullable = false)
//    private String district;
//
//    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false,
            columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
