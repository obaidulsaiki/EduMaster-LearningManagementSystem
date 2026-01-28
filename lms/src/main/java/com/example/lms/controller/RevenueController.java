package com.example.lms.controller;

import com.example.lms.entity.Admin;
import com.example.lms.entity.Teacher;
import com.example.lms.entity.WithdrawalRequest;
import com.example.lms.repository.AdminRepository;
import com.example.lms.repository.TeacherRepository;
import com.example.lms.service.WithdrawalService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/revenue")
@RequiredArgsConstructor
public class RevenueController {

    private final WithdrawalService withdrawalService;
    private final TeacherRepository teacherRepo;
    private final AdminRepository adminRepo;

    @GetMapping("/teacher/summary")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<RevenueSummary> getTeacherSummary(Authentication auth) {
        Teacher teacher = teacherRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        RevenueSummary summary = new RevenueSummary();
        summary.setBalance(teacher.getBalance());
        summary.setTotalEarned(teacher.getTotalEarnings());
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/admin/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RevenueSummary> getAdminSummary(Authentication auth) {
        Admin admin = adminRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        RevenueSummary summary = new RevenueSummary();
        summary.setBalance(admin.getBalance());
        summary.setTotalEarned(admin.getTotalEarnings());
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawalRequest> requestWithdrawal(
            Authentication auth,
            @RequestBody WithdrawalRequestDTO req) {

        String role = auth.getAuthorities().stream().findFirst().get().getAuthority().replace("ROLE_", "");
        return ResponseEntity.ok(withdrawalService.requestWithdrawal(auth, role, req.getAmount(), req.getMethod()));
    }

    @GetMapping("/my-withdrawals")
    public ResponseEntity<List<WithdrawalRequest>> getMyWithdrawals(Authentication auth) {
        String role = auth.getAuthorities().stream().findFirst().get().getAuthority().replace("ROLE_", "");
        return ResponseEntity.ok(withdrawalService.getMyWithdrawals(auth, role));
    }

    @GetMapping("/admin/all-pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WithdrawalRequest>> getAllPending() {
        return ResponseEntity.ok(withdrawalService.getAllPendingRequests());
    }

    @PostMapping("/admin/complete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WithdrawalRequest> complete(@PathVariable Long id, @RequestParam String txId) {
        return ResponseEntity.ok(withdrawalService.completeWithdrawal(id, txId));
    }

    @PostMapping("/sync")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> sync() {
        withdrawalService.syncRevenue();
        return ResponseEntity.ok("Revenue synchronization completed successfully");
    }

    @GetMapping("/admin/all-history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WithdrawalRequest>> getAllHistory() {
        return ResponseEntity.ok(withdrawalService.getAllHistory());
    }

    @Data
    public static class RevenueSummary {
        private BigDecimal balance;
        private BigDecimal totalEarned;
    }

    @Data
    public static class WithdrawalRequestDTO {
        private BigDecimal amount;
        private String method;
    }
}
