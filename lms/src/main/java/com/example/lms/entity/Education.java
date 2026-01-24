package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "educations")
@Data
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    private EducationType type; // UNIVERSITY, HSC, SSC

    private String instituteName;
    private String department;
    private String degree;
    private String groupName;
    private Integer startYear;
    private Integer endYear;
    private Double gpa;
}

