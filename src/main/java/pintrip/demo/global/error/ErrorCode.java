package pintrip.demo.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "sessionId를 찾을 수 없습니다."),
    DONG_NOT_FOUND(HttpStatus.NOT_FOUND, "dongId를 찾을 수 없습니다."),
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 동에 장소가 없습니다."),
    QUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 장소에 퀘스트가 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

    private final HttpStatus status;
    private final String message;
}
