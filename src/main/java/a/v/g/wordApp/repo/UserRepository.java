package a.v.g.wordApp.repo;

import a.v.g.wordApp.model.sec.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String name);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String name);
    boolean existsByEmail(String email);
//    Long updateUser(User user);
    User save(User user);
}
