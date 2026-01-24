package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "completed_lectures")
public class CompletedLecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Enrollment enrollment;
    @ManyToOne
    private Student student;
    @ManyToOne
    private Lecture lecture;
}
