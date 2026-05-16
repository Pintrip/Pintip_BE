package pintrip.demo.domain.session.dto;

import lombok.Getter;
import pintrip.demo.domain.session.entity.TripSession;
import java.time.LocalDateTime;

@Getter
public class TripSessionCreateResponse {
    private final String sessionId;
    private final Long dongId;
    private final LocalDateTime createdAt;

    public TripSessionCreateResponse(TripSession session) {
        this.sessionId = session.getId();
        this.dongId = session.getDong().getId();
        this.createdAt = session.getCreatedAt();
    }
}
