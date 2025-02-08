package a.v.g.wordApp.repo;

import a.v.g.wordApp.model.words.Word;
import a.v.g.wordApp.model.words.WordTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordTranslationRepository extends JpaRepository<WordTranslation, Long> {
}