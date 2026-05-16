package pintrip.global.health;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "헬스", description = "배포·연동 확인용")
@RestController
public class HealthController {

	@Operation(summary = "헬스 체크", description = "정상 시 {\"status\":\"UP\"}")
	@GetMapping("/health")
	public Map<String, String> health() {
		return Map.of("status", "UP");
	}

}
