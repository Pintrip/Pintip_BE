package pintrip.demo.domain.tripquest.dto;

import lombok.Getter;

@Getter
public class TripQuestUpdateRequest {
    private String discovery;
    private String review;
    private Boolean isCompleted;
}
