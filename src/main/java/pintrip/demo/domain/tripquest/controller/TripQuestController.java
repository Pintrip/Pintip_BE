package pintrip.demo.domain.tripquest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pintrip.demo.domain.tripquest.dto.TripQuestCreateRequest;
import pintrip.demo.domain.tripquest.dto.TripQuestCreateResponse;
import pintrip.demo.domain.tripquest.dto.TripQuestResponse;
import pintrip.demo.domain.tripquest.dto.TripQuestUpdateRequest;
import pintrip.demo.domain.tripquest.service.TripQuestService;
import java.util.List;

@RestController
@RequestMapping("/quests")
@RequiredArgsConstructor
public class TripQuestController {

    private final TripQuestService tripQuestService;

    @PostMapping
    public ResponseEntity<TripQuestCreateResponse> createTripQuest(
            @RequestHeader("X-Session-Id") String sessionId,
            @RequestBody TripQuestCreateRequest request) {
        return ResponseEntity.ok(tripQuestService.createTripQuest(sessionId, request));
    }

    @GetMapping
    public ResponseEntity<List<TripQuestResponse>> getTripQuests(
            @RequestHeader("X-Session-Id") String sessionId) {
        return ResponseEntity.ok(tripQuestService.getTripQuests(sessionId));
    }

    @PatchMapping("/{questId}")
    public ResponseEntity<TripQuestResponse> updateTripQuest(
            @RequestHeader("X-Session-Id") String sessionId,
            @PathVariable String questId,
            @RequestBody TripQuestUpdateRequest request) {
        return ResponseEntity.ok(tripQuestService.updateTripQuest(sessionId, questId, request));
    }

    @DeleteMapping("/{questId}")
    public ResponseEntity<Void> deleteTripQuest(
            @RequestHeader("X-Session-Id") String sessionId,
            @PathVariable String questId) {
        tripQuestService.deleteTripQuest(sessionId, questId);
        return ResponseEntity.noContent().build();
    }
}
