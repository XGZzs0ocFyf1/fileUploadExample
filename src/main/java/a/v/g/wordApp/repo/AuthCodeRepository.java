package a.v.g.wordApp.repo;

import a.v.g.wordApp.model.AuthCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {
    Optional<AuthCode> findByAuthCode(String authCode);
}
