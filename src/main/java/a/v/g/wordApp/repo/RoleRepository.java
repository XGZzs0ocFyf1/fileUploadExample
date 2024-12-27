package a.v.g.wordApp.repo;

import a.v.g.wordApp.model.sec.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
