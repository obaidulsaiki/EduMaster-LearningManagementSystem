package com.example.lms.service;

import com.example.lms.dto.TeacherProfileRequestDTO;
import com.example.lms.dto.TeacherProfileResponseDTO;
import com.example.lms.entity.Teacher;
import com.example.lms.entity.TeacherProfile;
import com.example.lms.repository.TeacherProfileRepository;
import com.example.lms.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherProfileService {

    private final TeacherRepository teacherRepository;
    private final TeacherProfileRepository profileRepository;

    /* ================= GET PROFILE ================= */
    public TeacherProfileResponseDTO getMyProfile(Authentication auth) {

        Teacher teacher = teacherRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        TeacherProfile profile = (TeacherProfile) profileRepository
                .findTeacherProfileByTeacher_Id(teacher.getId());
        if(profileRepository.findTeacherProfileByTeacher_Id(teacher.getId()) == null)
        {
            profile = new TeacherProfile();
            profile.setTeacher(teacher);
            profileRepository.save(profile);
        }


        TeacherProfileResponseDTO dto = new TeacherProfileResponseDTO();
        dto.setName(teacher.getName());
        dto.setEmail(teacher.getEmail());

        if (profile != null) {
            dto.setBio(profile.getBio());
            dto.setField(profile.getField());
            dto.setExperienceYears(profile.getExperienceYears());
            dto.setLinkedInUrl(profile.getLinkedInUrl());
            dto.setWebsite(profile.getWebsite());

            if (profile.getUniversities() != null) {
                dto.setUniversities(
                        List.of(profile.getUniversities().split(","))
                );
            }
        }

        return dto;
    }

    /* ================= SAVE / UPDATE ================= */
    public void saveProfile(
            Authentication auth,
            TeacherProfileRequestDTO dto
    ) {

        Teacher teacher = teacherRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        TeacherProfile profile = (TeacherProfile) profileRepository
                .findTeacherProfileByTeacher_Id(teacher.getId());

                if(profileRepository.findTeacherProfileByTeacher_Id(teacher.getId()) == null)
                {
                    profile = new TeacherProfile();
                    profileRepository.save(profile);
                }

        profile.setTeacher(teacher);
        profile.setBio(dto.getBio());
        profile.setField(dto.getField());
        profile.setExperienceYears(dto.getExperienceYears());
        profile.setLinkedInUrl(dto.getLinkedInUrl());
        profile.setWebsite(dto.getWebsite());

        if (dto.getUniversities() != null) {
            profile.setUniversities(String.join(",", dto.getUniversities()));
        }

        profileRepository.save(profile);
    }
}

