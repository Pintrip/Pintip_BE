package pintrip.global.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "에러 응답 (4xx/5xx)")
@Getter
public class ErrorResponse {

	@Schema(description = "에러 코드", example = "SESSION_NOT_FOUND")
	private final String code;

	@Schema(description = "사용자용 메시지", example = "세션을 찾을 수 없습니다.")
	private final String message;

	@Schema(description = "필드별 유효성 오류 목록 (VALIDATION_FAILED 시에만 포함)")
	private final List<FieldError> errors;

	public ErrorResponse(String code, String message) {
		this(code, message, List.of());
	}

	public ErrorResponse(String code, String message, List<FieldError> errors) {
		this.code = code;
		this.message = message;
		this.errors = errors;
	}

	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode.name(), errorCode.getMessage());
	}

	@Getter
	@AllArgsConstructor
	@Schema(description = "필드 유효성 오류")
	public static class FieldError {
		@Schema(description = "필드명", example = "dongId")
		private final String field;
		@Schema(description = "오류 메시지", example = "동네 ID는 필수입니다.")
		private final String message;
	}
}
