package com.example.lms.service;

import com.example.lms.dto.ReviewDTO;
import com.example.lms.entity.Course;
import com.example.lms.entity.Review;
import com.example.lms.entity.Student;
import com.example.lms.entity.Enrollment;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.EnrollmentRepository;
import com.example.lms.repository.ReviewRepository;
import com.example.lms.repository.StudentRepository;
import com.example.lms.repository.QuizResultRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
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
    private final QuizResultRepository quizResultRepository;

    // --- 1. Add a Review ---
    public ReviewDTO addReview(Authentication auth, ReviewDTO dto) {
        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!courseRepository.existsById(dto.getCourseId())) {
            throw new RuntimeException("Course not found");
        }

        Long studentId = student.getId();

        // Validation B: Must be Enrolled & Paid
        Enrollment enrollment = enrollmentRepository.findEnrollmentByStudent_IdAndCourse_CourseId(studentId,
                dto.getCourseId());
        if (enrollment == null) {
            throw new RuntimeException("You must be enrolled to review this course.");
        }
        if (!enrollment.isPaid()) {
            throw new RuntimeException("You can only review courses you have purchased.");
        }

        // Validation C: Must have passed the quiz
        boolean passed = quizResultRepository.findByStudent_IdAndQuiz_Course_CourseId(studentId, dto.getCourseId())
                .map(r -> r.getPassed())
                .orElse(false);
        if (!passed) {
            throw new RuntimeException("You must pass the course quiz (score 15/20) to leave a review.");
        }

        // Validation D: Rating 1-10
        if (dto.getRating() < 1 || dto.getRating() > 10) {
            throw new RuntimeException("Rating must be between 1 and 10.");
        }

        // Validation E: One Review per Course
        if (reviewRepository.existsByStudentIdAndCourseCourseId(studentId, dto.getCourseId())) {
            throw new RuntimeException("You have already reviewed this course.");
        }

        // Save Review
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
        if (!courseRepository.existsById(courseId)) {
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