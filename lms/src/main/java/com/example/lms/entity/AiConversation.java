package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ai_conversations")
public class AiConversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long convId;

    private LocalDateTime startedAt = LocalDateTime.now();
    private LocalDateTime endedAt;

    @Column(columnDefinition = "TEXT")
    private String lastMessageSnippet;

    @Column(columnDefinition = "TEXT")
    private String actionsJson;

    // Relation to Student
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
}