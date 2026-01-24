package com.example.lms.repository;


import com.example.lms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    // Find courses created by a specific teacher (for Teacher Dashboard)
    // Note: 'TeacherTId' matches the 'teacher' field in Course and 'tId' in Teacher entity
    List<Course> findCourseByTeacher_id(Long teacherId);

    // Find all courses that are published (for Student Catalog)
    List<Course> findByPublishedTrue();

    // Search for courses by title (Case insensitive)
    List<Course> findByTitleContainingIgnoreCase(String keyword);
    @Query("SELECT DISTINCT c.category FROM Course c WHERE c.published = true")
    List<String> findDistinctCategories();
    List<Course> findByCategory(String category);
    Arrays findByTeacherId(Long tId);
    @Query("""
        SELECT c FROM Course c
        WHERE (:search IS NULL OR
              LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    Page<Course> searchCourses(String search, Pageable pageable);
}