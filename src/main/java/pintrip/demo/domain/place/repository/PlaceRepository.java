package pintrip.demo.domain.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pintrip.demo.domain.place.entity.Place;
import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findAllByDongId(Long dongId);
}
