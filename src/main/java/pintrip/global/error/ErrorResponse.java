package pintrip.global.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "에러 응답 (4xx/5xx)")
@Getter
@RequiredArgsConstructor
public class ErrorResponse {

	@Schema(description = "에러 코드", example = "SESSION_NOT_FOUND")
	private final String code;

	@Schema(description = "사용자용 메시지", example = "sessionId를 찾을 수 없습니다.")
	private final String message;
}
