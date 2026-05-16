package pintrip.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pintrip.domain.image.entity.ImageCardQuest;

import java.util.Collection;
import java.util.List;

public interface ImageCardQuestRepository extends JpaRepository<ImageCardQuest, Long> {
    List<ImageCardQuest> findAllByImageCardIdInOrderByImageCardIdAscQuestOrderAsc(Collection<Long> imageCardIds);
}
