package com.example.lms.service;

import com.example.lms.dto.UpdateEmailDTO;
import com.example.lms.dto.UpdatePasswordDTO;
import com.example.lms.dto.UserPreferenceDTO;
import com.example.lms.entity.Student;
import com.example.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSettingsService {

    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /* ================= EMAIL ================= */
    public void updateEmail(Authentication auth, UpdateEmailDTO dto) {

        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        student.setEmail(dto.getEmail());
        studentRepository.save(student);
    }

    /* ================= PASSWORD ================= */
    public void updatePassword(Authentication auth, UpdatePasswordDTO dto) {

        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), student.getPassword())) {
            throw new RuntimeException("Invalid current password");
        }

        student.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        studentRepository.save(student);
    }

    /* ================= PREFERENCES ================= */
    public void updatePreferences(Authentication auth, UserPreferenceDTO dto) {

        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setDarkMode(dto.isDarkMode());
        student.setEmailNotifications(dto.isEmailNotifications());

        studentRepository.save(student);
    }

    /* ================= DELETE ================= */
    public void deleteAccount(Authentication auth) {

        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        studentRepository.delete(student);
    }
}
