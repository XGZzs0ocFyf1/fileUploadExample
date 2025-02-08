package a.v.g.wordApp.repo;

import a.v.g.wordApp.model.words.LearningStatusReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningStatusReferenceRepository extends JpaRepository<LearningStatusReference, Long> {
}