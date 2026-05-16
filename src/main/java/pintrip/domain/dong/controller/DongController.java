package pintrip.domain.dong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pintrip.domain.dong.dto.DongResponse;
import pintrip.domain.dong.service.DongContentService;
import pintrip.domain.dong.service.DongService;
import pintrip.domain.session.dto.ImageCardResponse;

import java.util.List;

@Tag(name = "동네", description = "동네 목록·이미지 카드 (세션 생성 전 단계)")
@RestController
@RequestMapping("/dongs")
@RequiredArgsConstructor
public class DongController {

    private final DongService dongService;
    private final DongContentService dongContentService;

    @Operation(summary = "동네 목록", description = "활성 동네 10개. 프론트 동 선택·랜덤 UI용")
    @GetMapping
    public ResponseEntity<List<DongResponse>> getDongs() {
        return ResponseEntity.ok(dongService.getActiveDongs());
    }

    @Operation(summary = "이미지 카드 목록", description = "동네별 이미지 카드 3개와 카드별 퀘스트 3개를 함께 반환. sessionId 전달 시 quests[].isCompleted 반영")
    @GetMapping("/{dongId}/image-cards")
    public ResponseEntity<List<ImageCardResponse>> getImageCards(
            @Parameter(description = "동네 ID") @PathVariable Long dongId,
            @Parameter(description = "세션 UUID (퀘스트 완료 여부 조회용, 선택)")
            @RequestParam(required = false) String sessionId) {
        return ResponseEntity.ok(dongContentService.getImageCards(dongId, sessionId));
    }
}
