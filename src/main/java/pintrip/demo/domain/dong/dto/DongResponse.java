package pintrip.demo.domain.dong.dto;

import lombok.Getter;
import pintrip.demo.domain.dong.entity.Dong;

@Getter
public class DongResponse {
    private final Long id;
    private final String name;
    private final String city;
    private final String district;
    private final String description;

    public DongResponse(Dong dong) {
        this.id = dong.getId();
        this.name = dong.getName();
        this.city = dong.getCity();
        this.district = dong.getDistrict();
        this.description = dong.getDescription();
    }
}
