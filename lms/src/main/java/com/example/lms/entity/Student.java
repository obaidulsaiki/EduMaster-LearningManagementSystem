package com.example.lms.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true) // equalsAndHashcode means the getter and setter of the parents class will also get resolved
@Data
@Entity
@Table(name = "students")
public class Student extends BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "student")
    List<Enrollment> enrollments;
    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private List<CourseProgress> progresses;
    private boolean darkMode = false;
    private boolean emailNotifications = true;

}
