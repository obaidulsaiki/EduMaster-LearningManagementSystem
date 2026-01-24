package com.example.lms.service;

import com.example.lms.dto.AdminProfileDTO;
import com.example.lms.entity.Admin;
import com.example.lms.entity.AdminProfile;
import com.example.lms.repository.AdminProfileRepository;
import com.example.lms.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminProfileService {

    private final AdminRepository adminRepository;
    private final AdminProfileRepository profileRepository;

    public AdminProfileDTO getMyProfile(Authentication auth) {

        Admin admin = adminRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        AdminProfile profile = profileRepository
                .findAdminProfileByAdmin_AdId(admin.getAdId())
                .orElse(null);

        AdminProfileDTO dto = new AdminProfileDTO();
        dto.setEmail(admin.getEmail());
        dto.setName(admin.getName());

        if (profile != null) {
            dto.setId(profile.getId());
            dto.setDesignation(profile.getDesignation());
        }

        return dto;
    }

    public AdminProfileDTO saveOrUpdate(Authentication auth, AdminProfileDTO dto) {

        Admin admin = adminRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        AdminProfile profile = profileRepository
                .findAdminProfileByAdmin_AdId(admin.getAdId())
                .orElseGet(() -> {
                    AdminProfile p = new AdminProfile();
                    p.setAdmin(admin);
                    return p;
                });

        profile.setDesignation(dto.getDesignation());

        AdminProfile saved = profileRepository.save(profile);

        AdminProfileDTO res = new AdminProfileDTO();
        res.setId(saved.getId());
        res.setDesignation(saved.getDesignation());
        res.setEmail(admin.getEmail());
        res.setName(admin.getName());

        return res;
    }
}
