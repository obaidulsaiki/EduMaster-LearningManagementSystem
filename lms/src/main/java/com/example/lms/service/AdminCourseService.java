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
                        .orElseThrow(() -> new RuntimeException("Course not found"))
        );
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
        dto.setId(c.getCourseId());
        dto.setTitle(c.getTitle());
        dto.setPrice(c.getPrice());
        dto.setPublished(Boolean.TRUE.equals(c.getPublished()));
        return dto;
    }
}
