package com.example.lms.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass // This means this class won't have its own table, but children will
public abstract class BaseUser {
    private String name;
    private String email;
    private String password;
    private boolean enabled = true;
    private LocalDateTime createdAt = LocalDateTime.now();
}