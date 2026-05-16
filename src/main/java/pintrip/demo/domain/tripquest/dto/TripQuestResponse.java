package pintrip.demo.domain.tripquest.dto;

import lombok.Getter;
import pintrip.demo.domain.tripquest.entity.TripQuest;

@Getter
public class TripQuestResponse {
    private final String id;
    private final String placeDescription;
    private final String questDescription;
    private final String discovery;
    private final String review;
    private final Boolean isCompleted;

    public TripQuestResponse(TripQuest tq) {
        this.id = tq.getId();
        this.placeDescription = tq.getPlaceDescription();
        this.questDescription = tq.getQuestDescription();
        this.discovery = tq.getDiscovery();
        this.review = tq.getReview();
        this.isCompleted = tq.getIsCompleted();
    }
}
