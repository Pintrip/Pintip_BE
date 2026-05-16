package pintrip.domain.quest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pintrip.domain.quest.entity.QuestTemplate;

public interface QuestTemplateRepository extends JpaRepository<QuestTemplate, String> {
}
