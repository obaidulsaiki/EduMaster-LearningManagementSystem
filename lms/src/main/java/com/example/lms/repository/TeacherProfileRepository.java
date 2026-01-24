package com.example.lms.repository;

import com.example.lms.entity.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {
    TeacherProfile findTeacherProfileByTeacher_Id(Long tId);
}
