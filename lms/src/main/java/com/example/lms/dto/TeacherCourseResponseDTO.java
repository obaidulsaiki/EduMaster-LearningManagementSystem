package com.example.lms.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeacherCourseResponseDTO {
    private Long courseId;
    private String title;
    private String category;
    private BigDecimal price;
    private Boolean published;
    private int lectureCount;
    private int enrolledStudents;
}
