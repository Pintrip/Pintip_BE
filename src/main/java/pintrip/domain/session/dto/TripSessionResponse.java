package pintrip.domain.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import pintrip.domain.dong.dto.DongResponse;
import pintrip.domain.quest.dto.QuestTemplateResponse;
import pintrip.domain.session.entity.TripSession;

import java.util.List;

@Schema(description = "세션 조회·복구 응답 (새로고침·외부 링크 복귀용)")
@Getter
public class TripSessionResponse {

    @Schema(description = "세션 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
    private final String sessionId;

    @Schema(description = "선택된 동네")
    private final DongResponse dong;

    @Schema(description = "세션 상태", example = "ACTIVE", allowableValues = {"ACTIVE", "COMPLETED", "EXPIRED"})
    private final String status;

    @Schema(description = "선택된 동네에 고정 매핑된 이미지 카드 목록")
    private final List<ImageCardResponse> imageCards;

    @Schema(description = "선택된 동네에서 사용 가능한 고정 퀘스트 목록")
    private final List<QuestTemplateResponse> quests;

    public TripSessionResponse(
            TripSession session,
            List<ImageCardResponse> imageCards,
            List<QuestTemplateResponse> quests
    ) {
        this.sessionId = session.getId();
        this.dong = session.getDong() != null ? new DongResponse(session.getDong()) : null;
        this.status = session.getStatus();
        this.imageCards = imageCards;
        this.quests = quests;
    }
}
