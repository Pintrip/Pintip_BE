package pintrip.demo.domain.session.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pintrip.demo.domain.session.dto.TripSessionCreateRequest;
import pintrip.demo.domain.session.dto.TripSessionCreateResponse;
import pintrip.demo.domain.session.dto.TripSessionResponse;
import pintrip.demo.domain.session.service.TripSessionService;

@RestController
@RequestMapping("/api/trip-sessions")
@RequiredArgsConstructor
public class TripSessionController {

    private final TripSessionService tripSessionService;

    @PostMapping
    public ResponseEntity<TripSessionCreateResponse> createSession(
            @Valid @RequestBody TripSessionCreateRequest request) {
        return ResponseEntity.ok(tripSessionService.createSession(request));
    }

    // 세션 조회
    @GetMapping
    public ResponseEntity<TripSessionResponse> getSession(
            @RequestHeader("X-Session-Id") String sessionId) {
        return ResponseEntity.ok(tripSessionService.getSession(sessionId));
    }

    // 단일 세션 조회
    @GetMapping("/{sessionId}")
    public ResponseEntity<TripSessionResponse> getSessionById(
            @PathVariable String sessionId) {
        return ResponseEntity.ok(tripSessionService.getSession(sessionId));
    }

    // 세션 완료
    @PatchMapping("/{sessionId}/complete")
    public ResponseEntity<Void> completeSession(@PathVariable String sessionId) {
        tripSessionService.completeSession(sessionId);
        return ResponseEntity.ok().build();
    }
}
