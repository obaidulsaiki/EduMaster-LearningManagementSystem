package com.example.lms.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QuizResultResponseDTO {
    private String studentName;
    private Integer score;
    private Boolean passed;
    private LocalDateTime completedAt;
}
