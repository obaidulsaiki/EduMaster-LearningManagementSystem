package com.example.lms.dto;

import lombok.Data;

@Data
public class AdminDashboardStatsDTO {
    private Long totalStudents;
    private Long totalTeachers;
    private Long totalCourses;
    // You can add more fields here later if needed (e.g., totalRevenue)
}