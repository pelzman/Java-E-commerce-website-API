package com.ecommerce.project;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repository.RoleRepository;
import com.ecommerce.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class DataInitialization {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Bean
    @Transactional
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {

            userRepository.deleteAll();
            roleRepository.deleteAll();
            // CREATING AND SETTING ROLES
            Role sellerRole = createRoleIfNotExist(AppRole.ROLE_SELLER);
            Role userRole = createRoleIfNotExist(AppRole.ROLE_USER);
            Role adminRole = createRoleIfNotExist(AppRole.ROLE_ADMIN);
            // Refresh roles from database to ensure they're managed entities
            sellerRole = roleRepository.findByRoleId(sellerRole.getRoleId()).orElse(sellerRole);
            userRole = roleRepository.findByRoleId(userRole.getRoleId()).orElse(userRole);
            adminRole = roleRepository.findByRoleId(adminRole.getRoleId()).orElse(adminRole);

            Set<Long> sellerRoles = Set.of(sellerRole.getRoleId());
            Set<Long> userRoles = Set.of(userRole.getRoleId());
            Set<Long> adminRoles = Set.of(userRole.getRoleId(), sellerRole.getRoleId(), adminRole.getRoleId());


            // CREATING USERS

            createUserWithRoleIfNotExist("user1", "user1@example.com", "userPassword", userRoles);
            createUserWithRoleIfNotExist("admin1", "admin1@example.com", "adminPassword", adminRoles);
            createUserWithRoleIfNotExist("seller1", "seller1@example.com", "sellerPassword", sellerRoles);


        };

    }

    private Role createRoleIfNotExist(AppRole roleEnum){
        return roleRepository.findByRoleName(roleEnum)
                .orElseGet(()->roleRepository.save(new Role(roleEnum)));
    }
    private void createUserWithRoleIfNotExist(String username, String email, String password, Set<Long>roleIds){
        if(!userRepository.existsByUsername(username)){
            Set<Role>managedRole = roleRepository.findAllById(roleIds)
                    .stream().collect(Collectors.toSet());
            User user = new User(username, email, encoder.encode(password));
            user.setRoles(managedRole);
//            managedRole.forEach(user::addRole);
            userRepository.save(user);
        }
    }
}









