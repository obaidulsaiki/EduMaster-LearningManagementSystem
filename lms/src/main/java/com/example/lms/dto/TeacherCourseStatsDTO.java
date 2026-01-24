package com.example.lms.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeacherCourseStatsDTO {

    private Long courseId;
    private String title;
    private BigDecimal price;

    private int enrolledStudents;
    private BigDecimal earnings;

    private Boolean published;
    private int totalLectures;
    private int avgCompletionPercentage;
}

