package pintrip.domain.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "세션 생성 요청 (최종 동네 확정 시점)")
@Getter
public class TripSessionCreateRequest {

    @NotNull
    @Schema(description = "사용자가 선택한 동네 ID (GET /dongs 목록의 id)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long dongId;

    @NotNull
    @Schema(description = "사용자가 선택한 이미지 카드 ID (dong_image_mappings.id)", example = "11", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long imageCardId;

    @NotNull
    @Schema(description = "사용자가 선택한 퀘스트 ID (image_card_quests.id)", example = "32", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long questId;
}
