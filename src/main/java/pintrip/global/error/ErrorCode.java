package pintrip.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "sessionId를 찾을 수 없습니다."),
    SESSION_EXPIRED(HttpStatus.GONE, "세션이 만료되었습니다."),
    DONG_NOT_FOUND(HttpStatus.NOT_FOUND, "dongId를 찾을 수 없습니다."),
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "동네에 연결된 이미지 카드를 찾을 수 없습니다."),
    QUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 퀘스트를 찾을 수 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

    private final HttpStatus status;
    private final String message;
}
