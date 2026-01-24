package com.example.lms.dto;

import lombok.Data;

import java.util.List;

@Data
public class LectureReorderDTO {
    private List<Long> lectureIds;
}

