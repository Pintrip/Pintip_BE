package pintrip.demo.domain.dong.dto;

import lombok.Getter;
import pintrip.demo.domain.dong.entity.Dong;

@Getter
public class DongResponse {
    private final Long id;
    private final String name;
    private final boolean active;

    public DongResponse(Dong dong) {
        this.id = dong.getId();
        this.name = dong.getName();
        this.active = dong.isActive();
    }
}
