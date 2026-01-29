package com.example.lms.service;

import com.example.lms.entity.VerificationCode;
import com.example.lms.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationCodeRepository repository;
    private final EmailService emailService;

    @Transactional
    public void generateAndSendCode(String email, VerificationCode.CodeType type) {
        // Delete any existing codes for this email and type
        repository.deleteByEmailAndType(email, type);

        // Generate 6-digit code
        String code = String.format("%06d", new Random().nextInt(999999));

        VerificationCode verificationCode = VerificationCode.builder()
                .email(email)
                .code(code)
                .type(type)
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .build();

        repository.save(verificationCode);
        emailService.sendVerificationCode(email, code);
    }

    public boolean verifyCode(String email, String code, VerificationCode.CodeType type) {
        return repository.findByEmailAndCodeAndType(email, code, type)
                .map(vc -> {
                    boolean isValid = vc.getExpiryDate().isAfter(LocalDateTime.now());
                    if (isValid) {
                        repository.delete(vc);
                    }
                    return isValid;
                })
                .orElse(false);
    }
}
