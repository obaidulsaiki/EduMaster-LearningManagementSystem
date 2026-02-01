package com.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrendDataDTO {
    private String name; // Day of week or date
    private long students;
    private BigDecimal revenue;
}
