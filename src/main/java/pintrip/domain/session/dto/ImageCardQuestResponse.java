package pintrip.domain.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import pintrip.domain.image.entity.ImageCardQuest;

@Getter
@Schema(description = "이미지 카드에 연결된 퀘스트")
public class ImageCardQuestResponse {

    @Schema(description = "퀘스트 문구", example = "낡은 간판 찾기")
    private final String quest;

    @Schema(description = "퀘스트 설명")
    private final String questDescription;

    public ImageCardQuestResponse(ImageCardQuest quest) {
        this.quest = quest.getQuestTitle();
        this.questDescription = quest.getQuestDescription();
    }
}
