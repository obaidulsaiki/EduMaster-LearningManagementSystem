package com.example.lms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private BigDecimal price;

    @Column(nullable = false)
    private Boolean published = false;

    /* ✅ NEW: CATEGORY (AI, Web, Data, etc.) */
    @Column(nullable = false)
    private String category;

    /* ✅ TIMESTAMPS */
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /* TEACHER RELATION */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseProgress> courseProgress;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Lecture> lectures;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Review> reviews;
    /* AUTO TIMESTAMPS */
    private int lecturesCount;
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
