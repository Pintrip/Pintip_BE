package pintrip.demo.domain.session.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TripSessionCreateRequest {
    @NotNull
    private Long dongId;
}
