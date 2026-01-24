package com.example.lms.controller;

import com.example.lms.dto.AdminProfileDTO;
import com.example.lms.service.AdminProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {

    private final AdminProfileService service;

    @GetMapping("/me")
    public AdminProfileDTO getMyProfile(Authentication auth) {
        return service.getMyProfile(auth);
    }

    @PostMapping
    public AdminProfileDTO saveOrUpdate(
            Authentication auth,
            @RequestBody AdminProfileDTO dto
    ) {
        return service.saveOrUpdate(auth, dto);
    }
}
