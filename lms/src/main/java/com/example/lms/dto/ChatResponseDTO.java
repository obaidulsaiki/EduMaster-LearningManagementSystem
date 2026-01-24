package com.example.lms.dto;

import lombok.Data;

@Data
public class ChatResponseDTO {
    private String reply;
    private String actionType; // e.g., "NAVIGATE", "SUGGEST_COURSE"
    private String actionPayload; // e.g., "/courses/java-101"
}