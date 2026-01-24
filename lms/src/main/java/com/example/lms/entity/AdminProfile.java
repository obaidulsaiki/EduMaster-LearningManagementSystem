package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "admin_profiles")
@Data
public class AdminProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false, unique = true)
    private Admin admin;

    private String designation;
}
