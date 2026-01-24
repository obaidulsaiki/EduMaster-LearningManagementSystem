package com.example.lms.dto;

import lombok.Data;
import java.util.List;

@Data
public class CourseFullDetailsDTO {
    private CourseDTO courseInfo;
    private List<LectureDTO> Lectures;
    // You might structure this to include lectures nested inside sections if you prefer
}