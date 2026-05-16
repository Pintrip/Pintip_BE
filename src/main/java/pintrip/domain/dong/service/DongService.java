package pintrip.domain.dong.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pintrip.domain.dong.dto.DongResponse;
import pintrip.domain.dong.repository.DongRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DongService {

    private final DongRepository dongRepository;

    public List<DongResponse> getActiveDongs() {
        return dongRepository.findAllByActiveTrue()
                .stream()
                .map(DongResponse::new)
                .collect(Collectors.toList());
    }
}
