package com.example.lms.dto;

import lombok.Data;

@Data
public class LectureResponseDTO {
    private Long id;
    private String title;
    private String videoUrl;
    private Integer orderIndex;
}
