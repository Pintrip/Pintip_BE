package pintrip.demo.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pintrip.demo.domain.place.entity.Place;
import pintrip.demo.domain.place.repository.PlaceRepository;
import pintrip.demo.global.error.BusinessException;
import pintrip.demo.global.error.ErrorCode;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;

    public Place getRandomPlaceByDongId(Long dongId) {
        List<Place> places = placeRepository.findAllByDongId(dongId);
        if (places.isEmpty()) {
            throw new BusinessException(ErrorCode.PLACE_NOT_FOUND);
        }
        return places.get(ThreadLocalRandom.current().nextInt(places.size()));
    }
}
