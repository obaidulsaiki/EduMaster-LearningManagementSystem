package com.example.lms.config;

import com.example.lms.entity.Admin;
import com.example.lms.repository.AdminRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Data
public class AdminSeeder {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner createAdmin() {
        return args -> {

            if (adminRepository.count() == 0) {

                Admin admin = new Admin();
                admin.setName("System Admin");
                admin.setEmail("admin@lms.com");
                admin.setPassword(
                        passwordEncoder.encode("admin123")
                );

                adminRepository.save(admin);

                System.out.println("✅ Default Admin created:");
                System.out.println("   Email: admin@lms.com");
                System.out.println("   Password: admin123");
            }
        };
    }
}
