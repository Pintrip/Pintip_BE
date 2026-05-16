package pintrip.domain.dong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pintrip.domain.dong.dto.DongResponse;
import pintrip.domain.dong.service.DongService;

import java.util.List;

@Tag(name = "동네", description = "동네 목록·랜덤 (세션 생성 전 단계)")
@RestController
@RequestMapping("/dongs")
@RequiredArgsConstructor
public class DongController {

    private final DongService dongService;

    @Operation(summary = "동네 목록", description = "활성 동네 10개. 프론트 동 선택·랜덤 UI용")
    @GetMapping
    public ResponseEntity<List<DongResponse>> getDongs() {
        return ResponseEntity.ok(dongService.getActiveDongs());
    }

    @Operation(summary = "동네 랜덤 1개", description = "활성 동네 중 무작위 1개 (선택 UI 보조용)")
    @GetMapping("/random")
    public ResponseEntity<DongResponse> getRandomDong() {
        return ResponseEntity.ok(dongService.getRandomDong());
    }
}
