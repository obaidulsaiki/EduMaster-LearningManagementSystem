package com.example.lms.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EnrollmentResponseDTO {
    private Long enrolId;
    private Long courseId;
    private String courseTitle;
    private Double progressPercentage;
    private LocalDateTime enrolledAt;
}