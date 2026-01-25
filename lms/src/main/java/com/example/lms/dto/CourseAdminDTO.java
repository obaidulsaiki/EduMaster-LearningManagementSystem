package com.example.lms.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseAdminDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private boolean published;
    private boolean enabled;
}
