package com.example.lms.dto;
import lombok.Data;
import java.util.List;

@Data
public class TeacherProfileRequestDTO {

    private String bio;
    private String field;
    private List<String> universities;
    private Integer experienceYears;
    private String linkedInUrl;
    private String website;
}


