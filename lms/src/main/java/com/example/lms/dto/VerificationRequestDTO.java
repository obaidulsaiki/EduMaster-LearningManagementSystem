package com.example.lms.dto;

import com.example.lms.entity.VerificationCode;
import lombok.Data;

@Data
public class VerificationRequestDTO {
    private VerificationCode.CodeType type;
}
