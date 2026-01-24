package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private Boolean isRead = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    // We keep it generic: "userId" could be Student ID or Teacher ID
    // or you can create separate fields like 'studentId'
    private Long recipientId;
    private String recipientRole; // "STUDENT" or "TEACHER"
}