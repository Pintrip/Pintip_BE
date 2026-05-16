package pintrip.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pintrip.domain.image.entity.DongImageMapping;

import java.util.List;

public interface DongImageMappingRepository extends JpaRepository<DongImageMapping, Long> {
    List<DongImageMapping> findAllByDongIdOrderByIdAsc(Long dongId);
}
