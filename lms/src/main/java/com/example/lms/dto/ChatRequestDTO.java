package com.example.lms.dto;

import lombok.Data;

@Data
public class ChatRequestDTO {
    private String message;
    private Long userId;
    private String userRole; // "STUDENT", "TEACHER", or "ADMIN"
}