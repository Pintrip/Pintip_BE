package pintrip.demo.domain.tripquest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pintrip.demo.domain.tripquest.entity.TripQuest;
import java.util.List;

public interface TripQuestRepository extends JpaRepository<TripQuest, String> {
    List<TripQuest> findBySessionId(String sessionId);
}
