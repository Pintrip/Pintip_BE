package pintrip.domain.session.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pintrip.domain.session.dto.QuestReviewResponse;
import pintrip.domain.session.dto.QuestReviewUpsertRequest;
import pintrip.domain.session.dto.TripSessionCreateRequest;
import pintrip.domain.session.dto.TripSessionCreateResponse;
import pintrip.domain.session.dto.TripSessionResponse;
import pintrip.domain.session.service.TripSessionQuestReviewService;
import pintrip.domain.session.service.TripSessionService;

import java.util.List;

@Tag(name = "여행 세션", description = "로그인 없음. sessionId(UUID)로 여행 상태 유지. 세션 만료 2일")
@RestController
@RequestMapping("/trip-sessions")
@RequiredArgsConstructor
public class TripSessionController {

    private final TripSessionService tripSessionService;
    private final TripSessionQuestReviewService questReviewService;

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

    @Operation(summary = "세션 완료", description = "status를 COMPLETED로 변경. 2일 만료·퀘스트 후기 3건 시에도 서버가 자동 COMPLETED 처리")
    @PatchMapping("/{sessionId}/complete")
    public ResponseEntity<Void> completeSession(
            @Parameter(description = "세션 UUID") @PathVariable String sessionId) {
        tripSessionService.completeSession(sessionId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "퀘스트 후기 저장/수정", description = "퀘스트 1개당 후기 1개. 동일 questId 재요청 시 수정")
    @PutMapping("/{sessionId}/quest-reviews/{questId}")
    public ResponseEntity<QuestReviewResponse> upsertQuestReview(
            @Parameter(description = "세션 UUID") @PathVariable String sessionId,
            @Parameter(description = "퀘스트 ID (image_card_quests.id)") @PathVariable Long questId,
            @Valid @RequestBody QuestReviewUpsertRequest request) {
        return ResponseEntity.ok(questReviewService.upsertReview(sessionId, questId, request));
    }

    @Operation(summary = "세션 퀘스트 후기 목록", description = "작성된 퀘스트 후기 전체 조회")
    @GetMapping("/{sessionId}/quest-reviews")
    public ResponseEntity<List<QuestReviewResponse>> getQuestReviews(
            @Parameter(description = "세션 UUID") @PathVariable String sessionId) {
        return ResponseEntity.ok(questReviewService.getReviews(sessionId));
    }

    @Operation(
            summary = "추가 퀘스트 뽑기 (P1 · 미구현)",
            description = "여행 중(세션 ACTIVE) 동일 동네·카드 풀에서 보너스 퀘스트 1개를 추가로 뽑는다. 비즈니스 로직 미구현."
    )
    @ApiResponse(responseCode = "501", description = "미구현")
    @PostMapping("/{sessionId}/bonus-quests")
    public ResponseEntity<Void> drawBonusQuest(
            @Parameter(description = "세션 UUID") @PathVariable String sessionId,
            @Parameter(description = "기준 imageCardId") @RequestParam Long imageCardId) {
        return notImplemented();
    }

    @Operation(
            summary = "비주류 점수 조회 (P1 · 미구현)",
            description = "장소 주류성·퀘스트 보너스·후기 완료율 등을 합산한 비주류 점수를 반환한다. 비즈니스 로직 미구현."
    )
    @ApiResponse(responseCode = "501", description = "미구현")
    @GetMapping("/{sessionId}/offbeat-score")
    public ResponseEntity<Void> getOffbeatScore(
            @Parameter(description = "세션 UUID") @PathVariable String sessionId) {
        return notImplemented();
    }

    @Operation(
            summary = "결과 카드 조회 (P1 · 미구현)",
            description = "완료 화면·SNS 공유용 카드 요약(동네, 카드, 퀘스트, 후기, 점수) JSON을 반환한다. 비즈니스 로직 미구현."
    )
    @ApiResponse(responseCode = "501", description = "미구현")
    @GetMapping("/{sessionId}/result-card")
    public ResponseEntity<Void> getResultCard(
            @Parameter(description = "세션 UUID") @PathVariable String sessionId) {
        return notImplemented();
    }

    private static ResponseEntity<Void> notImplemented() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
