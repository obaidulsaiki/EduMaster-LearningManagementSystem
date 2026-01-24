package com.example.lms.service;

import com.example.lms.dto.ReviewDTO;
import com.example.lms.entity.Course;
import com.example.lms.entity.Review;
import com.example.lms.entity.Student;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.EnrollmentRepository;
import com.example.lms.repository.ReviewRepository;
import com.example.lms.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    // --- 1. Add a Review ---
    public ReviewDTO addReview(ReviewDTO dto) {
        // Validation A: Check if Student and Course exist
        if(!studentRepository.existsById(dto.getStudentId())) {
            throw new RuntimeException("Student not found");
        }
        if(!courseRepository.existsById(dto.getCourseId())) {
            throw new RuntimeException("Course not found");
        }

        // Validation B: Must be Enrolled
        boolean isEnrolled = enrollmentRepository.existsEnrollmentByStudent_IdAndCourse_CourseId(dto.getStudentId(), dto.getCourseId());
        if (!isEnrolled) {
            throw new RuntimeException("You can only review courses you are enrolled in.");
        }

        // Validation C: One Review per Course [cite: 19]
        if (reviewRepository.existsByStudentIdAndCourseCourseId(dto.getStudentId(), dto.getCourseId())) {
            throw new RuntimeException("You have already reviewed this course.");
        }

        // Save Review
        Student student = studentRepository.findById(dto.getStudentId()).get();
        Course course = courseRepository.findById(dto.getCourseId()).get();

        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setStudent(student);
        review.setCourse(course);

        Review saved = reviewRepository.save(review);

        // Return DTO
        ReviewDTO response = new ReviewDTO();
        response.setStudentId(student.getId());
        response.setStudentName(student.getName());
        response.setCourseId(course.getCourseId());
        response.setRating(saved.getRating());
        response.setComment(saved.getComment());
        response.setCreatedAt(saved.getCreatedAt());

        return response;
    }

    // --- 2. Get Reviews for a Course ---
    public List<ReviewDTO> getCourseReviews(Long courseId) {
        if(!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Course not found");
        }

        return reviewRepository.findByCourseCourseId(courseId).stream().map(r -> {
            ReviewDTO dto = new ReviewDTO();
            dto.setStudentId(r.getStudent().getId());
            dto.setStudentName(r.getStudent().getName());
            dto.setRating(r.getRating());
            dto.setComment(r.getComment());
            dto.setCreatedAt(r.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }
}