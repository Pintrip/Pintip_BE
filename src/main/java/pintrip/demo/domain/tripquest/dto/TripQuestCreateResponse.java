package pintrip.demo.domain.tripquest.dto;

import lombok.Getter;
import pintrip.demo.domain.tripquest.entity.TripQuest;

@Getter
public class TripQuestCreateResponse {
    private final String id;
    private final String placeDescription;
    private final String questDescription;
    private final Boolean isCompleted;

    public TripQuestCreateResponse(TripQuest tq) {
        this.id = tq.getId();
        this.placeDescription = tq.getPlaceDescription();
        this.questDescription = tq.getQuestDescription();
        this.isCompleted = tq.getIsCompleted();
    }
}
