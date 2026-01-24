package com.example.lms.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeacherProfileResponseDTO {

    private Long tId;
    private String name;
    private String email;

    private String bio;
    private String field;
    private List<String> universities;
    private Integer experienceYears;
    private String linkedInUrl;
    private String website;
}

