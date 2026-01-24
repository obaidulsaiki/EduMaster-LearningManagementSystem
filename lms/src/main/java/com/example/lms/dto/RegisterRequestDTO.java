package com.example.lms.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String role;   // STUDENT | TEACHER
    private String name;
    private String email;
    private String password;
}
