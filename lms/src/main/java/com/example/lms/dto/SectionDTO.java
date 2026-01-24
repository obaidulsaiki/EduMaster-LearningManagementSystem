package com.example.lms.dto;

import lombok.Data;

@Data
public class SectionDTO {
    private Long secId;
    private String title;
    private Integer position;
    private Long courseId; // To link this section to a course
}