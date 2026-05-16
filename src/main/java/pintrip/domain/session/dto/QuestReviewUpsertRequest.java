package pintrip.domain.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Schema(description = "퀘스트 후기 저장/수정 요청")
@Getter
public class QuestReviewUpsertRequest {

    @NotNull
    @Schema(description = "이미지 카드 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long imageCardId;

    @NotBlank
    @Size(max = 500)
    @Schema(description = "발견한 것", example = "오래된 간판과 조용한 골목을 발견했다")
    private String discoveredNote;

    @NotBlank
    @Size(max = 2000)
    @Schema(description = "짧은 후기", example = "유명한 장소는 아니지만, 평범한 동네를 새롭게 보게 됐다")
    private String reviewText;

    @Schema(description = "이 장소는 어땠나요? (감정 태그)", example = "[\"조용했다\", \"다시 가고 싶다\"]")
    private List<String> moodTags;
}
