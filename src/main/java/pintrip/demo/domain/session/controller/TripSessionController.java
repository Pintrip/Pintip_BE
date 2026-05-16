package pintrip.demo.domain.session.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pintrip.demo.domain.place.dto.PlaceRandomResponse;
import pintrip.demo.domain.quest.dto.QuestRandomResponse;
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

    @GetMapping
    public ResponseEntity<TripSessionResponse> getSession(@PathVariable String sessionId) {
        return ResponseEntity.ok(tripSessionService.getSession(sessionId));
    }

    @PostMapping("/place/random")
    public ResponseEntity<PlaceRandomResponse> assignRandomPlace(@PathVariable String sessionId) {
        return ResponseEntity.ok(tripSessionService.assignRandomPlace(sessionId));
    }

    @PostMapping("/quest/random")
    public ResponseEntity<QuestRandomResponse> assignRandomQuest(@PathVariable String sessionId) {
        return ResponseEntity.ok(tripSessionService.assignRandomQuest(sessionId));
    }
}
