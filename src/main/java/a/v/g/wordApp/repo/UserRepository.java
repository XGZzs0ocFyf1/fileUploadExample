package a.v.g.wordApp.repo;

import a.v.g.wordApp.model.sec.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String name);
}
