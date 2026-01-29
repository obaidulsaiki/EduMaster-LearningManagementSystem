package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    private CodeType type;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    public enum CodeType {
        RESET_PASSWORD,
        CHANGE_EMAIL,
        CHANGE_PASSWORD
    }
}
