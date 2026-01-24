package com.example.lms.repository;

import com.example.lms.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Find admin by email for Login
    Optional<Admin> findByEmail(String email);

    // Check if email exists for Registration validation
    boolean existsByEmail(String email);
}
