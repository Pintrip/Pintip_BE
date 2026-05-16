package pintrip.demo.domain.place.dto;

import lombok.Getter;
import pintrip.demo.domain.place.entity.Place;

@Getter
public class PlaceResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;
    private final String mapKeyword;

    public PlaceResponse(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.description = place.getDescription();
        this.imageUrl = place.getImageUrl();
        this.mapKeyword = place.getMapKeyword();
    }
}
