package com.example.lms.controller;

import com.example.lms.dto.LectureDTO;
import com.example.lms.dto.LectureReorderDTO;
import com.example.lms.service.TeacherLectureService;
import com.example.lms.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherLectureController {

    private final TeacherLectureService lectureService;
    private final TeacherService teacherService;

    /* ================= LECTURES ================= */

    @GetMapping("/courses/{courseId}/lectures")
    public List<LectureDTO> getLectures(
            Authentication auth,
            @PathVariable Long courseId
    ) {
        return lectureService.getLectures(auth, courseId);
    }

    @PostMapping("/courses/{courseId}/lectures")
    public LectureDTO addLecture(
            Authentication auth,
            @PathVariable Long courseId,
            @RequestBody LectureDTO dto
    ) {
        return lectureService.addLecture(auth, courseId, dto);
    }

    @PutMapping("/lectures/{lectureId}")
    public LectureDTO updateLecture(
            Authentication auth,
            @PathVariable Long lectureId,
            @RequestBody LectureDTO dto
    ) {
        return lectureService.updateLecture(auth, lectureId, dto);
    }

    @DeleteMapping("/lectures/{lectureId}")
    public void deleteLecture(
            Authentication auth,
            @PathVariable Long lectureId
    ) {
        lectureService.deleteLecture(auth, lectureId);
    }

    @PutMapping("/courses/{courseId}/lectures/reorder")
    public void reorderLectures(
            Authentication auth,
            @PathVariable Long courseId,
            @RequestBody LectureReorderDTO dto
    ) {
        lectureService.reorder(auth, courseId, dto.getLectureIds());
    }
}
