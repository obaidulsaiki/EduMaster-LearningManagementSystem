package com.example.lms.dto;

import lombok.Data;
import java.util.List;

@Data
public class MentorDTO {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private String field;
    private Integer experienceYears;
    private List<TeacherExperienceDTO> experiences;
    private int courseCount;
}
