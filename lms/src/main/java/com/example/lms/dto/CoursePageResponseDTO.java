package com.example.lms.dto;

import lombok.Data;
import java.util.List;

@Data
public class CoursePageResponseDTO {

    private List<CourseDTO> courses;

    private int currentPage;
    private int totalPages;
    private long totalElements;
}
