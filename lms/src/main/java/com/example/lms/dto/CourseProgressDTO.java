package com.example.lms.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseProgressDTO {
    private Long courseId;
    private String courseTitle;
    private int progress;
    private boolean completed;
    private LocalDateTime completedAt;
}
