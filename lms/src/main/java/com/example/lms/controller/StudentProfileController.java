package com.example.lms.controller;

import com.example.lms.dto.StudentProfileDTO;
import com.example.lms.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/student/profile")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileService profileService;

    // ================= GET MY PROFILE =================
    // GET http://localhost:8080/api/student/profile/me
    @GetMapping("/me")
    public Map<String, Object> getMyProfile(Authentication authentication) {
        return profileService.getProfile(authentication);
    }

    // ================= CREATE / UPDATE PROFILE =================
    // POST /api/student/profile
    @PostMapping
    public void saveProfile(
            Authentication authentication,
            @RequestBody StudentProfileDTO dto
    ) {
        profileService.saveProfile(authentication, dto);
    }
}
