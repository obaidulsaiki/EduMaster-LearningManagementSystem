package com.example.lms.repository;

import com.example.lms.entity.TeacherEducation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherEducationRepository extends JpaRepository<TeacherEducation, Long> {
    List<TeacherEducation> findByTeacher_Id(Long teacherId);
}
