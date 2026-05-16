package pintrip.demo.domain.place.dto;

import lombok.Getter;
import pintrip.demo.domain.place.entity.Place;

@Getter
public class PlaceRandomResponse {
    private final PlaceResponse place;

    public PlaceRandomResponse(Place place) {
        this.place = new PlaceResponse(place);
    }
}
