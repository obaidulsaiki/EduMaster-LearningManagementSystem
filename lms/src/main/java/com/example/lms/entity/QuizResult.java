package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "quiz_results")
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    private Integer score;

    private Boolean passed;

    @Enumerated(EnumType.STRING)
    private QuizStatus status = QuizStatus.COMPLETED;

    private LocalDateTime completedAt;

    @PrePersist
    public void onCreate() {
        completedAt = LocalDateTime.now();
    }
}
