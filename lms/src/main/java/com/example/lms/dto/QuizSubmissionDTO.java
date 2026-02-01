package com.example.lms.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuizSubmissionDTO {
    private Long courseId;
    private List<AnswerDTO> answers;

    @Data
    public static class AnswerDTO {
        private Long questionId;
        private Integer selectedOptionIndex; // MCQ
        private String textResponse; // Fill in the Blanks
        private java.util.Map<String, String> matchingResponse; // Matching
        private Long assignmentSubmissionId; // Assignment
    }
}
