package com.example.lms.dto;

import lombok.Data;

@Data
public class TeacherResponseDTO {
    private Long tId;
    private String name;
    private String email;
    // You can add "bio" or "specialization" here later if you update the Entity
}