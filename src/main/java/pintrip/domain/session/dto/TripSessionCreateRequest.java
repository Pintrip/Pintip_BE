package pintrip.domain.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "세션 생성 요청 (동네·이미지 카드 확정 시점. 카드에 딸린 퀘스트 3개가 모두 세션에 포함됨)")
@Getter
public class TripSessionCreateRequest {

    @NotNull
    @Schema(description = "사용자가 선택한 동네 ID (GET /dongs 목록의 id)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long dongId;

    @NotNull
    @Schema(description = "사용자가 선택한 이미지 카드 ID (dong_image_mappings.id). 해당 카드의 퀘스트 3개 전체가 선택됨", example = "11", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long imageCardId;
}
