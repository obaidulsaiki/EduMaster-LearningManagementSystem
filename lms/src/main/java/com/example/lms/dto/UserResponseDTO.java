package com.example.lms.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role; // "STUDENT", "TEACHER", or "ADMIN"
}