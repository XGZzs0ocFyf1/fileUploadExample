package a.v.g.wordApp.repo;

import a.v.g.wordApp.model.words.UserWords;
import a.v.g.wordApp.model.words.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWordsRepository extends JpaRepository<UserWords, Long> {
}