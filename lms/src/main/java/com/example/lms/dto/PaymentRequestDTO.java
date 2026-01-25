package com.example.lms.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {
    private String transactionId;
    private String method; // bKash, Nagad, etc.
    private BigDecimal amount;
}
