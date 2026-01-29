package com.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponseDTO {
    private Long wishlistId;
    private Long courseId;
    private String title;
    private String category;
    private BigDecimal price;
    private String teacherName;
    private LocalDateTime addedAt;
}
