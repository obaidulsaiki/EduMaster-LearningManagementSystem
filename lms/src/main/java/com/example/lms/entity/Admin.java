package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "admins")
public class Admin extends BaseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id")
    private Long id;

    private java.math.BigDecimal balance = java.math.BigDecimal.ZERO;
    private java.math.BigDecimal totalEarnings = java.math.BigDecimal.ZERO;
}
