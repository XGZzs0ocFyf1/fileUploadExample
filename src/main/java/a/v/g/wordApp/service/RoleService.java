package a.v.g.wordApp.service;

import a.v.g.wordApp.model.sec.Role;
import a.v.g.wordApp.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Optional<Role> findByName(String name){
        return roleRepository.findByName(name);
    }
}
