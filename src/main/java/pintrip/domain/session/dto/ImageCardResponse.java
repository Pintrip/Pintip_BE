package pintrip.domain.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import pintrip.domain.image.entity.DongImageMapping;

import java.util.List;

@Schema(description = "동네에 연결된 이미지 카드(고정 데이터)")
@Getter
public class ImageCardResponse {

    @Schema(description = "이미지 카드 ID", example = "1")
    private final Long imageCardId;

    @Schema(description = "소속 동네 ID", example = "1")
    private final Long dongId;

    @Schema(description = "이미지 파일명", example = "Seongsu-Q01-Q05-Q18.jpg")
    private final String imageFile;

    @Schema(description = "카드 대표 문장", example = "오래된 공장의 흔적을 따라 걷는 길")
    private final String imageHeadline;

    @Schema(description = "카드 보조 설명")
    private final String imageSubDescription;

    @Schema(description = "해당 카드에 고정 매핑된 퀘스트 3개")
    private final List<ImageCardQuestResponse> quests;

    public ImageCardResponse(DongImageMapping mapping, List<ImageCardQuestResponse> quests) {
        this.imageCardId = mapping.getId();
        this.dongId = mapping.getDong().getId();
        this.imageFile = mapping.getImageFile();
        this.imageHeadline = mapping.getImageHeadline();
        this.imageSubDescription = mapping.getImageSubDescription();
        this.quests = quests;
    }
}
