package pintrip.demo.domain.quest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pintrip.demo.domain.quest.entity.Quest;
import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Long> {
    List<Quest> findAllByPlaceId(Long placeId);
}
