package pintrip.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ── 세션 ────────────────────────────────────────────────────────────────
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "세션을 찾을 수 없습니다."),
    SESSION_EXPIRED(HttpStatus.GONE, "만료된 세션입니다."),
    SESSION_COMPLETED(HttpStatus.GONE, "이미 완료된 세션입니다."),
    SESSION_DONG_MISMATCH(HttpStatus.BAD_REQUEST, "세션의 동네와 요청한 동네가 일치하지 않습니다."),

    // ── 동네 ────────────────────────────────────────────────────────────────
    DONG_NOT_FOUND(HttpStatus.NOT_FOUND, "동네를 찾을 수 없습니다."),
    DONG_INACTIVE(HttpStatus.NOT_FOUND, "현재 서비스하지 않는 동네입니다."),

    // ── 이미지 카드 ─────────────────────────────────────────────────────────
    IMAGE_CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "동네에 연결된 이미지 카드를 찾을 수 없습니다."),
    IMAGE_CARD_NOT_IN_DONG(HttpStatus.BAD_REQUEST, "해당 동네에 속하지 않는 이미지 카드입니다."),
    IMAGE_CARD_NOT_SELECTED(HttpStatus.BAD_REQUEST, "세션에서 선택된 이미지 카드와 일치하지 않습니다."),

    // ── 퀘스트 ──────────────────────────────────────────────────────────────
    QUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "퀘스트를 찾을 수 없습니다."),
    QUEST_DATA_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "퀘스트 데이터가 올바르지 않습니다. 관리자에게 문의하세요."),

    // ── 퀘스트 후기 ──────────────────────────────────────────────────────────
    QUEST_REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "퀘스트 후기는 1회만 작성할 수 있습니다."),

    // ── 공통 ────────────────────────────────────────────────────────────────
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    MISSING_REQUEST_HEADER(HttpStatus.BAD_REQUEST, "필수 헤더가 누락되었습니다."),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
