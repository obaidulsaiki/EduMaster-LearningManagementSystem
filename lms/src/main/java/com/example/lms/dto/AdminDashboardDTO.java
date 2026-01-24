package com.example.lms.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminDashboardDTO {
    private long totalStudents;
    private long totalTeachers;
    private long totalCourses;
    private BigDecimal totalRevenue;
}
