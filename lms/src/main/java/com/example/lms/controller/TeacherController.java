package com.example.lms.controller;

import com.example.lms.dto.*;
import com.example.lms.service.TeacherDashboardService;
import com.example.lms.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final TeacherDashboardService teacherDashboardService;

    /* ================= PROFILE ================= */

    @GetMapping("/profile/me")
    public TeacherProfileResponseDTO getMyProfile(Authentication auth) {
        return teacherService.getMyProfile(auth);
    }

    @PostMapping("/profile")
    public TeacherProfileResponseDTO saveOrUpdateProfile(
            Authentication auth,
            @RequestBody TeacherProfileRequestDTO dto
    ) {
        return teacherService.saveOrUpdateProfile(auth, dto);
    }

    /* ================= DASHBOARD ================= */

    @GetMapping("/dashboard")
    public TeacherDashboardDTO getDashboard(Authentication auth) {
        return teacherDashboardService.getDashboard(auth);
    }

    /* ================= EDUCATION ================= */

    @GetMapping("/education/me")
    public List<TeacherEducationDTO> getMyEducations(Authentication auth) {
        return teacherService.getMyEducations(auth);
    }

    @PostMapping("/education")
    public TeacherEducationDTO addEducation(
            Authentication auth,
            @RequestBody TeacherEducationDTO dto
    ) {
        return teacherService.addEducation(auth, dto);
    }

    @PutMapping("/education/{id}")
    public TeacherEducationDTO updateEducation(
            Authentication auth,
            @PathVariable Long id,
            @RequestBody TeacherEducationDTO dto
    ) {
        return teacherService.updateEducation(auth, id, dto);
    }

    @DeleteMapping("/education/{id}")
    public void deleteEducation(
            Authentication auth,
            @PathVariable Long id
    ) {
        teacherService.deleteEducation(auth, id);
    }

    /* ================= EXPERIENCE ================= */

    @GetMapping("/experience/me")
    public List<TeacherExperienceDTO> getMyExperiences(Authentication auth) {
        return teacherService.getMyExperiences(auth);
    }

    @PostMapping("/experience")
    public TeacherExperienceDTO addExperience(
            Authentication auth,
            @RequestBody TeacherExperienceDTO dto
    ) {
        return teacherService.addExperience(auth, dto);
    }

    @PutMapping("/experience/{id}")
    public TeacherExperienceDTO updateExperience(
            Authentication auth,
            @PathVariable Long id,
            @RequestBody TeacherExperienceDTO dto
    ) {
        return teacherService.updateExperience(auth, id, dto);
    }

    @DeleteMapping("/experience/{id}")
    public void deleteExperience(
            Authentication auth,
            @PathVariable Long id
    ) {
        teacherService.deleteExperience(auth, id);
    }
}
