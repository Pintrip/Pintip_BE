package pintrip.domain.dong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Operation(
            summary = "이미지 카드 다시 뽑기 (P1 · 미구현)",
            description = "동네 내 다른 이미지 카드를 다시 뽑는다. 일일/세션당 횟수 제한 예정. 비즈니스 로직 미구현."
    )
    @ApiResponse(responseCode = "501", description = "미구현")
    @PostMapping("/{dongId}/image-cards/reroll")
    public ResponseEntity<Void> rerollImageCard(
            @Parameter(description = "동네 ID") @PathVariable Long dongId,
            @Parameter(description = "현재 선택된 imageCardId (dong_image_mappings.id)")
            @RequestParam Long currentImageCardId,
            @Parameter(description = "세션 UUID (선택, 생성 후 재뽑기 시)")
            @RequestParam(required = false) String sessionId) {
        return notImplemented();
    }

    @Operation(
            summary = "퀘스트 다시 뽑기 (P1 · 미구현)",
            description = "확정한 이미지 카드 내 다른 퀘스트를 다시 뽑는다. 일일/세션당 횟수 제한 예정. 비즈니스 로직 미구현."
    )
    @ApiResponse(responseCode = "501", description = "미구현")
    @PostMapping("/{dongId}/image-cards/{imageCardId}/quests/reroll")
    public ResponseEntity<Void> rerollQuest(
            @Parameter(description = "동네 ID") @PathVariable Long dongId,
            @Parameter(description = "확정 imageCardId") @PathVariable Long imageCardId,
            @Parameter(description = "현재 선택된 questId (image_card_quests.id)")
            @RequestParam Long currentQuestId,
            @Parameter(description = "세션 UUID (선택)")
            @RequestParam(required = false) String sessionId) {
        return notImplemented();
    }

    private static ResponseEntity<Void> notImplemented() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
