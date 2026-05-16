package pintrip.demo.domain.tripquest.dto;

import lombok.Getter;

@Getter
public class TripQuestCreateRequest {
    private String placeDescription;
    private String questDescription;
}
