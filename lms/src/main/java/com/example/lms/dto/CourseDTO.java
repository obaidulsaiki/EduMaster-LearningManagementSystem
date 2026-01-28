package com.example.lms.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseDTO {
    private Long courseId;
    private String title;
    private String description;
    private BigDecimal price;
    private Boolean published;
    private String category;
    private String teacherName; // Frontend just needs the name, not full Teacher object
    private Long teacherId;
    private int lectureCount;// Useful for linking
    private double averageRating;
    private int totalRatings;
}