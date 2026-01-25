package com.example.lms.controller;

import com.example.lms.dto.MentorDTO;
import com.example.lms.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/teachers")
@RequiredArgsConstructor
public class PublicTeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public List<MentorDTO> getAllMentors() {
        return teacherService.getAllMentors();
    }
}
