package pintrip.domain.session.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pintrip.domain.quest.dto.QuestTemplateResponse;
import pintrip.domain.session.dto.ImageCardResponse;
import pintrip.domain.session.dto.TripSessionCreateRequest;
import pintrip.domain.session.dto.TripSessionResponse;
import pintrip.domain.session.service.TripSessionService;

import java.util.List;

@Tag(name = "여행 세션", description = "로그인 없음. sessionId(UUID)로 상태 유지. 최종 동네 기준 고정 카드·고정 퀘스트를 제공")
@RestController
@RequestMapping("/trip-sessions")
@RequiredArgsConstructor
public class TripSessionController {

    private final TripSessionService tripSessionService;

    @Operation(summary = "세션 생성", description = "최종 동네 확정 시 호출. sessionId와 함께 해당 동네의 고정 카드/고정 퀘스트 목록을 즉시 반환")
    @PostMapping
    public ResponseEntity<TripSessionResponse> createSession(
            @Valid @RequestBody TripSessionCreateRequest request) {
        return ResponseEntity.ok(tripSessionService.createSession(request));
    }

    @Operation(summary = "세션 조회 (헤더)", description = "X-Session-Id 헤더로 조회. 해당 동네의 고정 카드·고정 퀘스트 포함 복구")
    @GetMapping
    public ResponseEntity<TripSessionResponse> getSession(
            @Parameter(description = "POST /trip-sessions 로 받은 sessionId", required = true)
            @RequestHeader("X-Session-Id") String sessionId) {
        return ResponseEntity.ok(tripSessionService.getSession(sessionId));
    }

    @Operation(summary = "세션 조회 (path)", description = "GET /trip-sessions 와 동일, sessionId를 path로 전달")
    @GetMapping("/{sessionId}")
    public ResponseEntity<TripSessionResponse> getSessionById(
            @Parameter(description = "세션 UUID") @PathVariable String sessionId) {
        return ResponseEntity.ok(tripSessionService.getSession(sessionId));
    }

    @Operation(summary = "이미지 카드 목록", description = "세션의 동네에 매핑된 이미지 카드 3개를 고정 순서로 반환")
    @GetMapping("/{sessionId}/image-cards")
    public ResponseEntity<List<ImageCardResponse>> getImageCards(
            @Parameter(description = "세션 UUID") @PathVariable String sessionId) {
        return ResponseEntity.ok(tripSessionService.getImageCards(sessionId));
    }

    @Operation(summary = "퀘스트 목록", description = "세션의 동네에서 사용 가능한 퀘스트 목록을 고정 순서로 반환")
    @GetMapping("/{sessionId}/quests")
    public ResponseEntity<List<QuestTemplateResponse>> getQuestList(
            @Parameter(description = "세션 UUID") @PathVariable String sessionId) {
        return ResponseEntity.ok(tripSessionService.getQuestList(sessionId));
    }

    @Operation(summary = "세션 완료", description = "status를 COMPLETED로 변경")
    @PatchMapping("/{sessionId}/complete")
    public ResponseEntity<Void> completeSession(
            @Parameter(description = "세션 UUID") @PathVariable String sessionId) {
        tripSessionService.completeSession(sessionId);
        return ResponseEntity.ok().build();
    }
}
