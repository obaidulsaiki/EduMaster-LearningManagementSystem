package com.example.lms.service;

import com.example.lms.dto.StudentProfileDTO;
import com.example.lms.entity.Student;
import com.example.lms.entity.StudentProfile;
import com.example.lms.repository.StudentProfileRepository;
import com.example.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentProfileService {

    private final StudentProfileRepository profileRepository;
    private final StudentRepository studentRepository;

    // ================= GET PROFILE =================
    public Map<String, Object> getProfile(Authentication authentication) {

        Student student = studentRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentProfile profile = profileRepository
                .findByStudentId(student.getId())
                .orElse(null); // IMPORTANT: first time can be null

        Map<String, Object> response = new HashMap<>();

        response.put("name", student.getName());
        response.put("email", student.getEmail());
        response.put("role", "STUDENT");

        if (profile != null) {
            response.put("bio", profile.getBio());
            response.put("phone", profile.getPhone());
            response.put("location", profile.getLocation());
        }

        return response;
    }

    // ================= CREATE / UPDATE PROFILE =================
    public void saveProfile(Authentication authentication, StudentProfileDTO dto) {

        Student student = studentRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentProfile profile = profileRepository
                .findByStudentId(student.getId())
                .orElse(new StudentProfile());

        profile.setStudent(student);
        profile.setBio(dto.getBio());
        profile.setPhone(dto.getPhone());
        profile.setLocation(dto.getLocation());

        profileRepository.save(profile);
    }
}
