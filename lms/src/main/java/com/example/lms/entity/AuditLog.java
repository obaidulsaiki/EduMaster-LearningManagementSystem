package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long adminId;
    private String adminName;
    private String action; // e.g., BAN_USER, APPROVE_COURSE
    private String targetType; // e.g., STUDENT, COURSE
    private Long targetId;
    private String description;

    private LocalDateTime timestamp = LocalDateTime.now();
}
