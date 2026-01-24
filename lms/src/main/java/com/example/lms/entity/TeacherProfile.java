package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teacher_profiles")
@Data
public class TeacherProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* OWNER */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false, unique = true)
    private Teacher teacher;

    /* PROFILE DATA */
    @Column(length = 1500)
    private String bio;

    private String field; // AI, Web, Data, etc.

    @Column(length = 1000)
    private String universities;
    // Stored as CSV: "MIT, Stanford, Harvard"

    private Integer experienceYears;

    private String linkedInUrl;
    private String website;
}


