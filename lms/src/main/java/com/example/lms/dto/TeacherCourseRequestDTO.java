package com.example.lms.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeacherCourseRequestDTO {
    private String title;
    private String description;
    private String category;
    private BigDecimal price;
    private Boolean published;
}


