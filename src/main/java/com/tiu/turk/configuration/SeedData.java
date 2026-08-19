package com.tiu.turk.configuration;

import com.tiu.turk.user.common.entity.RoleEntity;
import com.tiu.turk.user.common.entity.UserEntity;
import com.tiu.turk.user.common.repository.RoleRepository;
import com.tiu.turk.user.common.repository.UserRepository;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SeedData {
    @Bean
    CommandLineRunner init(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder) {
        return args -> {
            RoleEntity adminRole;
            if (roleRepo.count() == 0L) {
                adminRole = new RoleEntity();
                adminRole.setName("ROLE_ADMIN");
                adminRole.setFullName("Administrator");
                roleRepo.save(adminRole);
                RoleEntity userRole = new RoleEntity();
                userRole.setName("ROLE_USER");
                userRole.setFullName("User");
                roleRepo.save(userRole);
            }
            if (userRepo.findByEmailIgnoreCase("admin@gmail.com").isEmpty()) {
                adminRole = roleRepo.findAll().stream().filter(r -> r.getName().equals("ROLE_ADMIN")).findFirst().orElseThrow();
                UserEntity adminUser = new UserEntity();
                adminUser.setEmail("admin@gmail.com");
                adminUser.setPassword(encoder.encode((CharSequence)"admin123"));
                adminUser.setRoles(Set.of(adminRole));
                adminUser.setFirstName("Ruslan");
                adminUser.setLastName("Sk");
                userRepo.save(adminUser);
            }
        };
    }
}

