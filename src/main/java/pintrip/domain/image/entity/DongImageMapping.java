package pintrip.domain.image.entity;

import jakarta.persistence.*;
import lombok.Getter;
import pintrip.domain.dong.entity.Dong;

@Getter
@Entity
@Table(name = "dong_image_mappings")
public class DongImageMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_id", nullable = false)
    private Dong dong;

    @Column(name = "image_file", nullable = false)
    private String imageFile;

    @Column(name = "image_headline", nullable = false)
    private String imageHeadline;

    @Column(name = "image_sub_description", nullable = false)
    private String imageSubDescription;
}
