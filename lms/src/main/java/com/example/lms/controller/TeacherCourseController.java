package com.example.lms.controller;

import com.example.lms.dto.CourseDTO;
import com.example.lms.dto.TeacherCourseRequestDTO;
import com.example.lms.dto.TeacherCourseResponseDTO;
import com.example.lms.service.TeacherCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher/courses")
@RequiredArgsConstructor
public class TeacherCourseController {

    private final TeacherCourseService service;

    @PostMapping
    public TeacherCourseResponseDTO create(
            Authentication auth,
            @RequestBody TeacherCourseRequestDTO dto
    ) {
        return service.createCourse(auth, dto);
    }

    @GetMapping("/my")
    public List<CourseDTO> myCourses(
            Authentication auth
    ) {
        return service.getMyCourses(auth);
    }

    @PutMapping("/{courseId}")
    public TeacherCourseResponseDTO update(
            Authentication auth,
            @PathVariable Long courseId,
            @RequestBody TeacherCourseRequestDTO dto
    ) {
        return service.updateCourse(auth, courseId, dto);
    }

    @DeleteMapping("/{courseId}")
    public void delete(
            Authentication auth,
            @PathVariable Long courseId
    ) {
        service.deleteCourse(auth, courseId);
    }
    @PutMapping("/{courseId}/publish")
    public void publishCourse(
            Authentication auth,
            @PathVariable Long courseId
    ) {
        service.publishCourse(auth, courseId);
    }

    @PutMapping("/{courseId}/unpublish")
    public void unpublishCourse(
            Authentication auth,
            @PathVariable Long courseId
    ) {
        service.unpublishCourse(auth, courseId);
    }

}

