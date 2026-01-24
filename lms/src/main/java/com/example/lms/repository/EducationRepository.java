package com.example.lms.repository;

import com.example.lms.entity.Education;
import com.example.lms.entity.EducationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByStudentId(Long studentId);
    List<Education> findByStudentIdAndType(Long studentId, EducationType type);
}
