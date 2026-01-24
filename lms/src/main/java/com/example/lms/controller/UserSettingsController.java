package com.example.lms.controller;

import com.example.lms.dto.UpdateEmailDTO;
import com.example.lms.dto.UpdatePasswordDTO;
import com.example.lms.dto.UserPreferenceDTO;
import com.example.lms.service.UserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserSettingsController {

    private final UserSettingsService settingsService;

    @PutMapping("/email")
    public void updateEmail(
            Authentication auth,
            @RequestBody UpdateEmailDTO dto
    ) {
        settingsService.updateEmail(auth, dto);
    }

    @PutMapping("/password")
    public void updatePassword(
            Authentication auth,
            @RequestBody UpdatePasswordDTO dto
    ) {
        settingsService.updatePassword(auth, dto);
    }

    @PutMapping("/preferences")
    public void updatePreferences(
            Authentication auth,
            @RequestBody UserPreferenceDTO dto
    ) {
        settingsService.updatePreferences(auth, dto);
    }

    @DeleteMapping
    public void deleteAccount(Authentication auth) {
        settingsService.deleteAccount(auth);
    }
}
