package pintrip.domain.dong.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pintrip.domain.dong.dto.DongResponse;
import pintrip.domain.dong.entity.Dong;
import pintrip.domain.dong.repository.DongRepository;
import pintrip.global.error.BusinessException;
import pintrip.global.error.ErrorCode;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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

    // 동 랜덤생성
    public DongResponse getRandomDong() {
        List<Dong> dongs = dongRepository.findAllByActiveTrue();
        if (dongs.isEmpty()) {
            throw new BusinessException(ErrorCode.DONG_NOT_FOUND);
        }
        Dong randomDong = dongs.get(ThreadLocalRandom.current().nextInt(dongs.size()));
        return new DongResponse(randomDong);
    }
}
