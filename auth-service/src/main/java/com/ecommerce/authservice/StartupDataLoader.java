package com.ecommerce.authservice;

import com.ecommerce.authservice.entity.Role;
import com.ecommerce.authservice.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupDataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public StartupDataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        ensureRoleExists("ROLE_USER");
        ensureRoleExists("ROLE_ADMIN");
    }

    private void ensureRoleExists(String roleName) {
        roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = new Role();
            role.setName(roleName);
            return roleRepository.save(role);
        });
    }
}


