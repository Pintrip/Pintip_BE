package pintrip.domain.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import pintrip.domain.session.entity.TripSession;

import java.time.LocalDateTime;

@Schema(description = "세션 생성 응답")
@Getter
public class TripSessionCreateResponse {

    @Schema(description = "세션 UUID. 이후 API에 X-Session-Id 헤더 또는 path로 전달", example = "550e8400-e29b-41d4-a716-446655440000")
    private final String sessionId;

    @Schema(description = "선택된 동네 ID", example = "1")
    private final Long dongId;

    @Schema(description = "세션 생성 시각")
    private final LocalDateTime createdAt;

    public TripSessionCreateResponse(TripSession session) {
        this.sessionId = session.getId();
        this.dongId = session.getDong().getId();
        this.createdAt = session.getCreatedAt();
    }
}
