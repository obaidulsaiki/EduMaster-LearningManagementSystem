package com.example.lms.service;

import com.example.lms.dto.CourseAdminDTO;
import com.example.lms.entity.Course;
import com.example.lms.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCourseService {

    private final CourseRepository courseRepository;

    public Page<CourseAdminDTO> getCourses(int page, String search) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        return courseRepository
                .searchCourses(search, pageable)
                .map(this::toDto);
    }

    public CourseAdminDTO getCourse(Long id) {
        return toDto(
                courseRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Course not found")));
    }

    public void toggleStatus(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setEnabled(!course.isEnabled());
        courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public void togglePublish(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setPublished(!course.getPublished());
        courseRepository.save(course);
    }

    private CourseAdminDTO toDto(Course c) {
        CourseAdminDTO dto = new CourseAdminDTO();

        // Basic Info
        dto.setCourseId(c.getCourseId());
        dto.setTitle(c.getTitle());
        dto.setCategory(c.getCategory());
        dto.setPrice(c.getPrice());
        dto.setPublished(Boolean.TRUE.equals(c.getPublished()));
        dto.setEnabled(c.isEnabled());

        // Teacher Information
        if (c.getTeacher() != null) {
            dto.setTeacherId(c.getTeacher().getId());
            dto.setTeacherName(c.getTeacher().getName());
        }

        // Course Metrics
        dto.setLecturesCount(c.getLecturesCount());

        // Enrollment Count
        int enrollmentCount = (c.getEnrollments() != null) ? c.getEnrollments().size() : 0;
        dto.setEnrollmentsCount(enrollmentCount);

        // Calculate Total Revenue (from paid enrollments)
        java.math.BigDecimal totalRevenue = java.math.BigDecimal.ZERO;
        if (c.getEnrollments() != null) {
            totalRevenue = c.getEnrollments().stream()
                    .filter(e -> e.isPaid())
                    .map(e -> c.getPrice())
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        }
        dto.setTotalRevenue(totalRevenue);

        // Calculate Average Rating
        if (c.getReviews() != null && !c.getReviews().isEmpty()) {
            double avgRating = c.getReviews().stream()
                    .mapToInt(r -> r.getRating())
                    .average()
                    .orElse(0.0);
            dto.setAverageRating(avgRating);
            dto.setReviewsCount(c.getReviews().size());
        } else {
            dto.setAverageRating(0.0);
            dto.setReviewsCount(0);
        }

        // Timestamps
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());

        return dto;
    }
}
