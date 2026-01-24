package com.example.lms.dto;

import lombok.Data;

@Data
public class EnrollmentRequestDTO {
    private Long studentId;
    private Long courseId;
}