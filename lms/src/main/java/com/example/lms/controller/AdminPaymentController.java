package com.example.lms.controller;

import com.example.lms.entity.Payment;
import com.example.lms.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
public class AdminPaymentController {

    private final PaymentRepository paymentRepository;

    @GetMapping
    public List<AdminPaymentDTO> getAllPayments() {
        return paymentRepository.findAll(Sort.by(Sort.Direction.DESC, "paidAt"))
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private AdminPaymentDTO mapToDTO(Payment p) {
        AdminPaymentDTO dto = new AdminPaymentDTO();
        dto.setId(p.getId());
        dto.setStudentName(p.getEnrollment().getStudent().getName());
        dto.setStudentEmail(p.getEnrollment().getStudent().getEmail());
        dto.setCourseTitle(p.getEnrollment().getCourse().getTitle());
        dto.setAmount(p.getAmount());
        dto.setMethod(p.getPaymentMethod());
        dto.setTransactionId(p.getTransactionId());
        dto.setPaidAt(p.getPaidAt().toString());
        dto.setStatus(p.getSslStatus());

        java.math.BigDecimal amount = p.getAmount();
        dto.setTeacherShare(amount.multiply(new java.math.BigDecimal("0.85")));
        dto.setAdminShare(amount.multiply(new java.math.BigDecimal("0.15")));

        return dto;
    }

    @lombok.Data
    public static class AdminPaymentDTO {
        private Long id;
        private String studentName;
        private String studentEmail;
        private String courseTitle;
        private java.math.BigDecimal amount;
        private java.math.BigDecimal teacherShare;
        private java.math.BigDecimal adminShare;
        private String method;
        private String transactionId;
        private String paidAt;
        private String status;
    }
}
