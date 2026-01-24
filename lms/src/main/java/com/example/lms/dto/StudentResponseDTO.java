package com.example.lms.dto;

import lombok.Data;

@Data
public class StudentResponseDTO {
    private Long sId;
    private String name;
    private String email;
    // You could add "enrolledCoursesCount" here later if needed
}