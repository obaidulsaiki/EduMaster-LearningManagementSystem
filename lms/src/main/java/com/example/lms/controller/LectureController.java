package com.example.lms.controller;

import com.example.lms.dto.LectureResponseDTO;
import com.example.lms.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    /* ================= STUDENT ================= */

    // List lectures of a course
    @GetMapping("/course/{courseId}")
    public List<LectureResponseDTO> getLecturesByCourse(
            @PathVariable Long courseId
    ) {
        return lectureService.getLecturesByCourse(courseId);
    }

    // Get single lecture
    @GetMapping("/{lectureId}")
    public LectureResponseDTO getLecture(
            @PathVariable Long lectureId
    ) {
        return lectureService.getLecture(lectureId);
    }

}

