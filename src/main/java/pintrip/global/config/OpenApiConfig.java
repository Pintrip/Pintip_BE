package pintrip.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI pintripOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Pintrip API")
						.description("""
								핀트립 백엔드 API. 로그인 없음 — sessionId(UUID)로 여행 상태 유지.

								**플로우:** GET /dongs → GET /dongs/{dongId}/image-cards → POST /trip-sessions → PUT .../quest-reviews/{questId}

								**세션 만료:** 생성 후 2일 (`TripSessionPolicy.EXPIRE_DAYS`)

								**이미지:** API는 imageFile(파일명)만 반환. 실제 파일은 프론트 정적 자산에서 로드.
								""")
						.version("1.0.0"));
	}
}
