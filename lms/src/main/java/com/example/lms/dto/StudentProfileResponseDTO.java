package com.example.lms.dto;

import lombok.Data;

@Data
public class StudentProfileResponseDTO {
    private Long id;
    private String bio;
    private String phone;
    private String location;
}