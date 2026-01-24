package com.example.lms.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long studentId;
    private String studentName; // To show who wrote it
    private Long courseId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}