package pintrip.domain.session.entity;

import jakarta.persistence.*;
import lombok.Getter;
import pintrip.domain.image.entity.DongImageMapping;
import pintrip.domain.image.entity.ImageCardQuest;
import pintrip.domain.session.converter.StringListJsonConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(
        name = "trip_session_quest_reviews",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_session_quest_review",
                columnNames = {"session_id", "quest_id"}
        )
)
public class TripSessionQuestReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private TripSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_card_id", nullable = false)
    private DongImageMapping imageCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id", nullable = false)
    private ImageCardQuest quest;

    @Column(name = "discovered_note", nullable = false, length = 500)
    private String discoveredNote;

    @Column(name = "review_text", nullable = false, length = 2000)
    private String reviewText;

    @Convert(converter = StringListJsonConverter.class)
    @Column(name = "mood_tags", columnDefinition = "json")
    private List<String> moodTags = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected TripSessionQuestReview() {
    }

    public static TripSessionQuestReview create(
            TripSession session,
            DongImageMapping imageCard,
            ImageCardQuest quest,
            String discoveredNote,
            String reviewText,
            List<String> moodTags
    ) {
        TripSessionQuestReview review = new TripSessionQuestReview();
        review.session = session;
        review.imageCard = imageCard;
        review.quest = quest;
        review.discoveredNote = discoveredNote;
        review.reviewText = reviewText;
        review.moodTags = moodTags != null ? new ArrayList<>(moodTags) : new ArrayList<>();
        review.createdAt = LocalDateTime.now();
        review.updatedAt = review.createdAt;
        return review;
    }
}
