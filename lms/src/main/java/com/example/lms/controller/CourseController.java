package com.example.lms.controller;

import com.example.lms.dto.CourseDTO;
import com.example.lms.dto.CourseDetailsDTO;
import com.example.lms.dto.CoursePageResponseDTO;
import com.example.lms.dto.LectureDTO;
import com.example.lms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    /* ======================================================
       =============== PUBLIC (STUDENTS) ====================
       ====================================================== */

    // 🔍 Browse & filter courses
    @GetMapping("/filter")
    public ResponseEntity<CoursePageResponseDTO> getFilteredCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sort
    ) {
        return ResponseEntity.ok(
                courseService.getFilteredCourses(
                        page, size, category, minPrice, maxPrice, sort
                )
        );
    }

    // 📄 Course details page
    @GetMapping("/{courseId}")
    public CourseDetailsDTO getCourseDetails(@PathVariable Long courseId) {
        return courseService.getCourseDetails(courseId);
    }


    // 🏷 Categories
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(courseService.getAllCategories());
    }
}
