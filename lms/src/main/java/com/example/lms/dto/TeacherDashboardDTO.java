package com.example.lms.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TeacherDashboardDTO {

    private BigDecimal totalEarnings;
    private int totalStudents;
    private int totalCourses;
    private int publishedCourses;

    private List<TeacherCourseStatsDTO> courses;
}

