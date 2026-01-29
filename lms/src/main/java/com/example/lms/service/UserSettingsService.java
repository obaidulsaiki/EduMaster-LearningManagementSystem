package com.example.lms.service;

import com.example.lms.dto.*;
import com.example.lms.entity.Student;
import com.example.lms.entity.VerificationCode;
import com.example.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSettingsService {

    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final VerificationService verificationService;

    /* ================= EMAIL ================= */
    @Transactional
    public void updateEmail(Authentication auth, UpdateEmailDTO dto) {

        if (!verificationService.verifyCode(auth.getName(), dto.getCode(), VerificationCode.CodeType.CHANGE_EMAIL)) {
            throw new RuntimeException("Invalid or expired verification code");
        }

        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        student.setEmail(dto.getEmail());
        studentRepository.save(student);
    }

    /* ================= PASSWORD ================= */
    @Transactional
    public void updatePassword(Authentication auth, UpdatePasswordDTO dto) {

        if (!verificationService.verifyCode(auth.getName(), dto.getCode(), VerificationCode.CodeType.CHANGE_PASSWORD)) {
            throw new RuntimeException("Invalid or expired verification code");
        }

        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), student.getPassword())) {
            throw new RuntimeException("Invalid current password");
        }

        student.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        studentRepository.save(student);
    }

    public void requestVerification(Authentication auth, VerificationRequestDTO dto) {
        verificationService.generateAndSendCode(auth.getName(), dto.getType());
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
