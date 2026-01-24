package com.example.lms.repository;

import com.example.lms.entity.CourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseProgressRepository extends JpaRepository<CourseProgress, Long> {
    Optional<CourseProgress> findByStudentIdAndCourse_CourseId(Long sId, Long courseId);
    List<CourseProgress> findByStudentIdAndProgress(Long sId, Integer progress);
    List<CourseProgress> findByCourse_CourseId(Long courseId);

    long countByCourse_CourseId(Long courseId);

    @Query("""
        SELECT COALESCE(AVG(cp.progress), 0)
        FROM CourseProgress cp
        WHERE cp.course.courseId = :courseId
    """)
    Double getAverageProgressByCourseId(@Param("courseId") Long courseId);
}
