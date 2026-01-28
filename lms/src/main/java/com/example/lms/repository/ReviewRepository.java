package com.example.lms.repository;

import com.example.lms.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Get all reviews for a specific course
    List<Review> findByCourseCourseId(Long courseId);

    // Check if student already reviewed this course (Requirement: max one review
    // per course [cite: 19])
    boolean existsByStudentIdAndCourseCourseId(Long studentId, Long courseId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.course.courseId = :courseId")
    Double getAverageRating(Long courseId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.course.courseId = :courseId")
    Integer getTotalReviews(Long courseId);
}