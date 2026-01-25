package com.example.lms.dto;

import lombok.Data;

@Data
public class ChatResponseDTO {
    private String reply;
    private String actionType; // NAVIGATE, SUGGEST_COURSE, NONE
    private String actionPayload; // Path or ID
}