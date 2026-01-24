package com.example.lms.controller;

import com.example.lms.dto.CourseProgressDTO;
import com.example.lms.dto.ResumeCourseDTO;
import com.example.lms.service.CourseProgressService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/course")
@RequiredArgsConstructor
public class CourseProgressController {

    private final CourseProgressService courseProgressService;

    // GET /api/student/course/progress/{courseId}
    @GetMapping("/progress/{courseId}")
    public CourseProgressDTO getCourseProgress(
            Authentication authentication,
            @PathVariable Long courseId
    ) {
        return courseProgressService.getCourseProgress(authentication, courseId);
    }
    // POST http://localhost:8080/api/student/course/lecture/{lectureId}/complete
    @PostMapping("/lecture/{lectureId}/complete")
    public void completeLecture(
            @PathVariable Long lectureId,
            Authentication authentication
    ) {
        courseProgressService.completeLecture(authentication, lectureId);
    }
    // GET http://localhost:8080/api/student/course/{courseId}/resume
    @GetMapping("/{courseId}/resume")
    public ResumeCourseDTO resumeCourse(
            @PathVariable Long courseId,
            Authentication authentication
    ) {
        return courseProgressService.getResumeData(authentication, courseId);
    }

    // GET http://localhost:8080/api/student/course/completed
    @GetMapping("/completed")
    public List<CourseProgressDTO> completedCourses(
            Authentication authentication
    ) {
        return courseProgressService.getCompletedCourses(authentication);
    }
}
