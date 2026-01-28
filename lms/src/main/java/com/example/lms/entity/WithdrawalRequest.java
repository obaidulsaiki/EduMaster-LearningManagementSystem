package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "withdrawal_requests")
public class WithdrawalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // Can be Teacher ID or Admin ID
    private String userRole; // TEACHER or ADMIN

    private BigDecimal amount;

    private String status = "PENDING"; // PENDING, APPROVED, REJECTED, COMPLETED

    private String paymentMethod; // e.g., BKASH, NAGAD, BANK

    private String transactionId; // From SSLCommerz or Manual

    private LocalDateTime requestedAt = LocalDateTime.now();

    private LocalDateTime processedAt;
}
