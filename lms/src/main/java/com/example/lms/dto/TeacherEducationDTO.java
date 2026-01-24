package com.example.lms.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TeacherEducationDTO {
    private Long id;
    private String institution;
    private String degree;
    private String major;
    private LocalDate startDate;
    private LocalDate endDate;
}

