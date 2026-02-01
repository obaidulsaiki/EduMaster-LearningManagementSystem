package com.example.lms.dto;

import lombok.Data;

@Data
public class StudentAdminDTO {
    private Long id;
    private String name;
    private String email;
    private boolean enabled;
    private java.time.LocalDateTime createdAt;
}
