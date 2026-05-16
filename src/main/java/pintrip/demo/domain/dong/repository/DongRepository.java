package pintrip.demo.domain.dong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pintrip.demo.domain.dong.entity.Dong;
import java.util.List;

public interface DongRepository extends JpaRepository<Dong, Long> {
    List<Dong> findAllByActiveTrue();
}
