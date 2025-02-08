package a.v.g.wordApp.repo;

import a.v.g.wordApp.model.ImageLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageLinkRepository extends JpaRepository<ImageLink, Long> {
}