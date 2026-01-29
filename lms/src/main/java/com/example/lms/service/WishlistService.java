package com.example.lms.service;

import com.example.lms.dto.WishlistResponseDTO;
import com.example.lms.entity.Course;
import com.example.lms.entity.Student;
import com.example.lms.entity.Wishlist;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.StudentRepository;
import com.example.lms.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public String toggleWishlist(Authentication auth, Long courseId) {
        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (wishlistRepository.existsByStudentAndCourse(student, course)) {
            wishlistRepository.deleteByStudentAndCourse(student, course);
            return "Removed from wishlist";
        } else {
            wishlistRepository.save(new Wishlist(student, course));
            return "Added to wishlist";
        }
    }

    public List<WishlistResponseDTO> getStudentWishlist(Authentication auth) {
        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return wishlistRepository.findByStudent(student).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public boolean isCourseInWishlist(Authentication auth, Long courseId) {
        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return wishlistRepository.existsByStudentAndCourse(student, course);
    }

    private WishlistResponseDTO mapToDTO(Wishlist wishlist) {
        Course course = wishlist.getCourse();
        return WishlistResponseDTO.builder()
                .wishlistId(wishlist.getId())
                .courseId(course.getCourseId())
                .title(course.getTitle())
                .category(course.getCategory())
                .price(course.getPrice())
                .teacherName(course.getTeacher() != null ? course.getTeacher().getName() : "Unknown")
                .addedAt(wishlist.getAddedAt())
                .build();
    }
}
