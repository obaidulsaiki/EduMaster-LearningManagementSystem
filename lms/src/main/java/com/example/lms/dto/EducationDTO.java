package com.example.lms.dto;


import com.example.lms.entity.EducationType;
import lombok.Data;

@Data
public class EducationDTO {
    private Long id;
    private EducationType type;
    private String instituteName;
    private String department;
    private String degree;
    private String group;
    private Integer startYear;
    private Integer endYear;
    private Double gpa;
}