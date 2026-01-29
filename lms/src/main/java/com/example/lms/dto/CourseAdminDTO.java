package com.example.lms.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CourseAdminDTO {
    private Long courseId;
    private String title;
    private String category;
    private BigDecimal price;
    private boolean published;
    private boolean enabled;

    // Teacher Information
    private Long teacherId;
    private String teacherName;

    // Course Metrics
    private int lecturesCount;
    private int enrollmentsCount;
    private BigDecimal totalRevenue;

    // Ratings
    private Double averageRating;
    private int reviewsCount;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
