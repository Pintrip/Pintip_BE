package pintrip.domain.session.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pintrip.domain.session.dto.TripSessionCreateRequest;
import pintrip.domain.session.dto.TripSessionCreateResponse;
import pintrip.domain.session.dto.TripSessionResponse;
import pintrip.domain.session.service.TripSessionService;

@Tag(name = "여행 세션", description = "로그인 없음. sessionId(UUID)로 여행 상태 유지")
@RestController
@RequestMapping("/trip-sessions")
@RequiredArgsConstructor
public class TripSessionController {

    private final TripSessionService tripSessionService;

    @Operation(summary = "세션 생성", description = "동네·카드·퀘스트 UI 완료 후 최종 확정 시 호출")
    @PostMapping
    public ResponseEntity<TripSessionCreateResponse> createSession(
            @Valid @RequestBody TripSessionCreateRequest request) {
        return ResponseEntity.ok(tripSessionService.createSession(request));
    }

    @Operation(summary = "세션 조회 (헤더)", description = "X-Session-Id 헤더로 조회. 카드·퀘스트 목록은 GET /dongs/{dongId}/... 로 재조회")
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

    @Operation(summary = "세션 완료", description = "status를 COMPLETED로 변경")
    @PatchMapping("/{sessionId}/complete")
    public ResponseEntity<Void> completeSession(
            @Parameter(description = "세션 UUID") @PathVariable String sessionId) {
        tripSessionService.completeSession(sessionId);
        return ResponseEntity.ok().build();
    }
}
