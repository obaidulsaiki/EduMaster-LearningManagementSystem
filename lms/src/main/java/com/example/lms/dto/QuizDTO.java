package com.example.lms.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuizDTO {
    private Long id;
    private Long courseId;
    private List<QuestionDTO> questions;
}
