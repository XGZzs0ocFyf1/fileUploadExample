package a.v.g.wordApp.repo;

import a.v.g.wordApp.model.sec.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("""
            SELECT t FROM Token t inner join User u
            on t.user.user_id = u.user_id
            where t.user.user_id = :userId and t.loggedOut = false
            """)
    List<Token> findAllAccessTokenByUser(Long userId);

    Optional<Token> findByAccessToken(String accessToken);

    Optional<Token> findByRefreshToken(String refreshToken);
}