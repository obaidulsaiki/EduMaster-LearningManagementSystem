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
    private java.util.List<TrendDataDTO> trendData;
    private java.util.List<CategoryDataDTO> categoryData;

    private Double revenueTrend;
    private Double studentsTrend;
    private Double statusTrend; // For completed courses
    private Double commissionTrend;
}
