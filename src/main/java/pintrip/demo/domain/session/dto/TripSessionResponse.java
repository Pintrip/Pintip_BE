package pintrip.demo.domain.session.dto;

import lombok.Getter;
import pintrip.demo.domain.dong.dto.DongResponse;
import pintrip.demo.domain.place.dto.PlaceResponse;
import pintrip.demo.domain.quest.dto.QuestResponse;
import pintrip.demo.domain.session.entity.TripSession;

@Getter
public class TripSessionResponse {
    private final DongResponse dong;
    private final PlaceResponse place;
    private final QuestResponse quest;
    private final String status;

    public TripSessionResponse(TripSession session) {
        this.dong = session.getDong() != null ? new DongResponse(session.getDong()) : null;
        this.place = session.getSelectedPlace() != null ? new PlaceResponse(session.getSelectedPlace()) : null;
        this.quest = session.getSelectedQuest() != null ? new QuestResponse(session.getSelectedQuest()) : null;
        this.status = session.getStatus();
    }
}
