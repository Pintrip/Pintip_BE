package pintrip.demo.domain.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pintrip.demo.domain.session.entity.TripSession;

public interface TripSessionRepository extends JpaRepository<TripSession, String> {
}
