package a.v.g.wordApp.service;

import a.v.g.wordApp.model.sec.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.RoleNotFoundException;

public interface UserService {
    UserDetails loadUserByUsername(String username);
    void createNewUser(User user) throws RoleNotFoundException;
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
