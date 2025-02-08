package a.v.g.wordApp.repo;

import a.v.g.wordApp.model.words.LearningStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningStatusRepository extends JpaRepository<LearningStatus, Long> {
}
