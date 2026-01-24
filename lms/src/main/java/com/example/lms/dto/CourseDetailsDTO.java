package com.example.lms.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CourseDetailsDTO {

    // ===== COURSE =====
    private Long courseId;
    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private boolean published;

    // ===== TEACHER =====
    private Long teacherId;
    private String teacherName;
    private String teacherEmail;
    private String teacherBio;

    // ===== LECTURES =====
    private int lectureCount;
    private List<LectureResponseDTO> lectures;
}

