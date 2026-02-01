package com.example.lms.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String questionText;
    private String type; // MULTIPLE_CHOICE, MATCHING, etc.
    private String extraData; // JSON string for specialized configs
    private List<String> options;
    private Integer correctOptionIndex; // Only for teachers
}
