package pintrip.demo.domain.session.dto;

import lombok.Getter;
import pintrip.demo.domain.dong.dto.DongResponse;
import pintrip.demo.domain.session.entity.TripSession;

@Getter
public class TripSessionResponse {
    private final String sessionId;
    private final DongResponse dong;
    private final String status;

    public TripSessionResponse(TripSession session) {
        this.sessionId = session.getId();
        this.dong = session.getDong() != null ? new DongResponse(session.getDong()) : null;
        this.status = session.getStatus();
    }
}
