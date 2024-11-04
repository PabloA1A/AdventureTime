package dev.pabloabad.adventuretime.roles;

import javax.management.relation.RoleNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class RoleService {
    
    RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public Role getById(Long id) {
        Role role = repository.findById(id).orElseThrow(() -> new RoleNotFoundException("Role not found"));
        return role;
    }
}
