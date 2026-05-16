package pintrip.domain.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import pintrip.domain.session.entity.TripSessionQuestReview;

import java.time.LocalDateTime;

@Schema(description = "퀘스트 후기 응답")
@Getter
public class QuestReviewResponse {

    @Schema(description = "후기 ID")
    private final Long reviewId;

    @Schema(description = "세션 UUID")
    private final String sessionId;

    @Schema(description = "이미지 카드 ID")
    private final Long imageCardId;

    @Schema(description = "퀘스트 ID (image_card_quests.id)")
    private final Long questId;

    @Schema(description = "퀘스트 문구")
    private final String quest;

    @Schema(description = "퀘스트 설명")
    private final String questDescription;

    @Schema(description = "발견한 것")
    private final String discoveredNote;

    @Schema(description = "짧은 후기")
    private final String reviewText;

    @Schema(description = "퀘스트 완료(후기 작성) 여부")
    private final boolean isCompleted;

    @Schema(description = "최종 수정 시각")
    private final LocalDateTime updatedAt;

    public QuestReviewResponse(TripSessionQuestReview review) {
        this.reviewId = review.getId();
        this.sessionId = review.getSession().getId();
        this.imageCardId = review.getImageCard().getId();
        this.questId = review.getQuest().getId();
        this.quest = review.getQuest().getQuestTitle();
        this.questDescription = review.getQuest().getQuestDescription();
        this.discoveredNote = review.getDiscoveredNote();
        this.reviewText = review.getReviewText();
        this.isCompleted = true;
        this.updatedAt = review.getUpdatedAt();
    }
}
