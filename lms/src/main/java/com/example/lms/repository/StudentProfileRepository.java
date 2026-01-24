package com.example.lms.repository;

import com.example.lms.entity.Student;
import com.example.lms.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByStudentId(Long studentId);
    boolean existsByStudentId(Long studentId);
}
