package com.example.lms.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyReportDTO {
    private BigDecimal revenue;
    private long newStudents;
    private long completedCourses;
    private long activeStudents;
    private java.math.BigDecimal adminCommission;
    private java.math.BigDecimal teacherEarnings;
}
