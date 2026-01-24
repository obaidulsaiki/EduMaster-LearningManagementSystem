package com.example.lms.repository;

import com.example.lms.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // Find teacher by email (for Login)
    Optional<Teacher> findByEmail(String email);

    @Query("""
        SELECT t FROM Teacher t
        WHERE (:search IS NULL OR
              LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
              LOWER(t.email) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    Page<Teacher> searchTeachers(String search, Pageable pageable);
    // Check if email exists (for Registration)
    boolean existsByEmail(String email);
}