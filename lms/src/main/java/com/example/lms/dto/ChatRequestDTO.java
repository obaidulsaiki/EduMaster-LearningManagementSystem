package com.example.lms.dto;

import lombok.Data;

@Data
public class ChatRequestDTO {
    private Long userId;
    private String message;
}