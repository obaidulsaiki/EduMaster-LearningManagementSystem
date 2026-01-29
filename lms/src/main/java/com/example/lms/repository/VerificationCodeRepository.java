package com.example.lms.repository;

import com.example.lms.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByEmailAndCodeAndType(String email, String code, VerificationCode.CodeType type);

    void deleteByEmailAndType(String email, VerificationCode.CodeType type);
}
