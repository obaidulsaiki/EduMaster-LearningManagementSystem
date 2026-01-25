package com.example.lms.repository;

import com.example.lms.entity.TeacherEducation;
import com.example.lms.entity.TeacherExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherExperienceRepository extends JpaRepository<TeacherExperience, Long> {
    List<TeacherExperience> findByTeacher_Id(Long teacherId);
}
