package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    private String transactionId;

    private String paymentMethod; // bKash, Nagad, Card, etc.

    private BigDecimal amount;

    private String currency = "BDT";

    private LocalDateTime paidAt;

    private String sslStatus; // VALID, FAILED, CANCELLED
}
