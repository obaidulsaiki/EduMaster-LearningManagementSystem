package com.example.lms.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeacherAdminDTO {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private List<EducationDTO> educations;
    private List<TeacherExperienceDTO> experiences;
    private boolean enabled;
}

