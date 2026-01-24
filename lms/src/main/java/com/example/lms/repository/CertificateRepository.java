package com.example.lms.repository;

import com.example.lms.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificateRepository
        extends JpaRepository<Certificate, Long> {

    Optional<Certificate> findByStudentIdAndCourse_CourseId(
            Long studentId,
            Long courseId
    );

    boolean existsByStudentIdAndCourse_CourseId(
            Long studentId,
            Long courseId
    );
}

