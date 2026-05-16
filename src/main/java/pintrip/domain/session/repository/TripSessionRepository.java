package pintrip.domain.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pintrip.domain.session.entity.TripSession;

public interface TripSessionRepository extends JpaRepository<TripSession, String> {
}
