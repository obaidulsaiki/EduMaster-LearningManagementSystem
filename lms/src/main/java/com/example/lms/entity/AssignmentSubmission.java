package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "assignment_submissions")
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;

    private LocalDateTime submittedAt = LocalDateTime.now();

    private String feedback;
    private Integer pointsEarned;
}
