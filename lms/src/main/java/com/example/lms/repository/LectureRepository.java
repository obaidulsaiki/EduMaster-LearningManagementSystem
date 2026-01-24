package com.example.lms.repository;


import com.example.lms.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    
    // Change 'Id' to 'CourseId' to match the field name in Course entity
    List<Lecture> findByCourse_CourseIdOrderByOrderIndexAsc(Long courseId);

    List<Lecture> findByCourse_CourseId(Long CourseId);

    int countByCourse_CourseId(Long courseId);

    // Alternatively, if you prefer using the object:
    // List<Lecture> findByCourseOrderByOrderIndexAsc(Course course);
}