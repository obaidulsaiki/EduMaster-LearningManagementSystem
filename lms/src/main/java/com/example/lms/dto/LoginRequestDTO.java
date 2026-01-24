package com.example.lms.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String role;     // ADMIN | TEACHER | STUDENT
    private String email;
    private String password;
}
