package com.example.lms.controller;

import com.example.lms.dto.CourseAdminDTO;
import com.example.lms.service.AdminCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final AdminCourseService service;

    @GetMapping
    public Page<CourseAdminDTO> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search
    ) {
        return service.getCourses(page, search);
    }

    @GetMapping("/{id}")
    public CourseAdminDTO get(@PathVariable Long id) {
        return service.getCourse(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteCourse(id);
    }

    @PutMapping("/{id}/publish")
    public void togglePublish(@PathVariable Long id) {
        service.togglePublish(id);
    }
}
