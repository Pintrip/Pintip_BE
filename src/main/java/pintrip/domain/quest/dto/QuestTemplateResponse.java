package pintrip.domain.quest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import pintrip.domain.quest.entity.QuestTemplate;

@Schema(description = "퀘스트 템플릿 (Q01~Q18)")
@Getter
public class QuestTemplateResponse {

    @Schema(description = "퀘스트 코드", example = "Q01")
    private final String code;

    @Schema(description = "퀘스트 제목", example = "낡은 간판 찾기")
    private final String title;

    @Schema(description = "수행 가이드 문구")
    private final String description;

    public QuestTemplateResponse(QuestTemplate template) {
        this.code = template.getCode();
        this.title = template.getTitle();
        this.description = template.getDescription();
    }
}
