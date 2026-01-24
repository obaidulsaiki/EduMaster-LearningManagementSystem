package com.example.lms.dto;

import lombok.Data;

@Data
public class LectureRequestDTO {
    private String title;
    private String videoUrl;
    private Integer orderIndex;
}
