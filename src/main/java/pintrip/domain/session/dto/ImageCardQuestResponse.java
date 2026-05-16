package pintrip.domain.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import pintrip.domain.image.entity.ImageCardQuest;

@Getter
@Schema(description = "이미지 카드에 연결된 퀘스트")
public class ImageCardQuestResponse {

    @Schema(description = "퀘스트 ID (후기 API path에 사용)", example = "1")
    private final Long questId;

    @Schema(description = "퀘스트 문구", example = "낡은 간판 찾기")
    private final String quest;

    @Schema(description = "퀘스트 설명")
    private final String questDescription;

    @Schema(description = "세션에서 후기 작성 완료 여부 (sessionId 쿼리 전달 시)")
    private final boolean isCompleted;

    public ImageCardQuestResponse(ImageCardQuest quest) {
        this(quest, false);
    }

    public ImageCardQuestResponse(ImageCardQuest quest, boolean isCompleted) {
        this.questId = quest.getId();
        this.quest = quest.getQuestTitle();
        this.questDescription = quest.getQuestDescription();
        this.isCompleted = isCompleted;
    }
}
