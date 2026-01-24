package com.example.lms.repository;

import com.example.lms.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("""
        SELECT s FROM Student s
        WHERE (:search IS NULL OR
              LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
              LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    Page<Student> searchStudents(String search, Pageable pageable);
}