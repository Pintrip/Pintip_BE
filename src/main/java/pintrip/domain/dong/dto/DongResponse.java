package pintrip.domain.dong.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import pintrip.domain.dong.entity.Dong;

@Schema(description = "동네(동) 정보")
@Getter
public class DongResponse {

    @Schema(description = "동네 ID", example = "1")
    private final Long id;

    @Schema(description = "동네 이름", example = "성수동")
    private final String name;

    @Schema(description = "목록 노출 여부", example = "true")
    private final boolean active;

    public DongResponse(Dong dong) {
        this.id = dong.getId();
        this.name = dong.getName();
        this.active = dong.isActive();
    }
}
