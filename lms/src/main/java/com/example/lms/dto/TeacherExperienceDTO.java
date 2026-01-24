package com.example.lms.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TeacherExperienceDTO {
    private Long id;
    private String company;
    private String designation;
    private LocalDate startDate;
    private LocalDate endDate;
}

