package pintrip.domain.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import pintrip.domain.session.entity.TripSession;

import java.time.LocalDateTime;

@Schema(description = "세션 만료 조회·처리 응답")
@Getter
public class TripSessionExpiredResponse {

    @Schema(description = "세션 UUID")
    private final String sessionId;

    @Schema(description = "세션 상태", allowableValues = {"ACTIVE", "COMPLETED", "EXPIRED"})
    private final String status;

    @Schema(description = "더 이상 사용할 수 없는 세션인지 (EXPIRED·COMPLETED 또는 TTL 경과)")
    private final boolean expired;

    @Schema(description = "TTL 만료 시각 (생성 후 2일)")
    private final LocalDateTime expiredAt;

    public TripSessionExpiredResponse(TripSession session) {
        this.sessionId = session.getId();
        this.status = session.getStatus();
        this.expiredAt = session.getExpiredAt();
        this.expired = !session.isUsable();
    }
}
