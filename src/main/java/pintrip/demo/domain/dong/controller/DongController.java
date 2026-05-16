package pintrip.demo.domain.dong.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pintrip.demo.domain.dong.dto.DongResponse;
import pintrip.demo.domain.dong.service.DongService;
import java.util.List;

@RestController
@RequestMapping("/dongs")
@RequiredArgsConstructor
public class DongController {

    private final DongService dongService;

    @GetMapping
    public ResponseEntity<List<DongResponse>> getDongs() {
        return ResponseEntity.ok(dongService.getActiveDongs());
    }
}
