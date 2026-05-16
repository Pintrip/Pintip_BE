package pintrip.domain.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pintrip.domain.session.entity.TripSessionQuestReview;

import java.util.List;
import java.util.Optional;

public interface TripSessionQuestReviewRepository extends JpaRepository<TripSessionQuestReview, Long> {

    Optional<TripSessionQuestReview> findBySession_IdAndQuest_Id(String sessionId, Long questId);

    List<TripSessionQuestReview> findAllBySession_IdOrderByImageCard_IdAscQuest_QuestOrderAsc(String sessionId);

    long countBySession_Id(String sessionId);
}
