package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity

@Data
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private Student student;

    private String bio;
    private String phone;
    private String location;
}
