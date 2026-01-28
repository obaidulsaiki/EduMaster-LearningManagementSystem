package com.example.lms.service;

import com.example.lms.entity.Admin;
import com.example.lms.entity.Teacher;
import com.example.lms.entity.WithdrawalRequest;
import com.example.lms.repository.AdminRepository;
import com.example.lms.repository.TeacherRepository;
import com.example.lms.repository.WithdrawalRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WithdrawalService {

    private final WithdrawalRequestRepository withdrawalRepo;
    private final TeacherRepository teacherRepo;
    private final AdminRepository adminRepo;
    private final com.example.lms.repository.EnrollmentRepository enrollmentRepo;
    private final com.example.lms.repository.CourseRepository courseRepo;

    @Transactional
    public void syncRevenue() {
        // 1. Sync Teachers
        List<Teacher> teachers = teacherRepo.findAll();
        for (Teacher teacher : teachers) {
            BigDecimal totalEarned = BigDecimal.ZERO;
            List<com.example.lms.entity.Course> courses = courseRepo.findCourseByTeacher_id(teacher.getId());
            for (com.example.lms.entity.Course course : courses) {
                BigDecimal courseRevenue = enrollmentRepo.sumPaidAmountByCourseId(course.getCourseId());
                if (courseRevenue != null) {
                    totalEarned = totalEarned.add(courseRevenue);
                }
            }

            // 85% for Teacher
            BigDecimal teacherEarnings = totalEarned.multiply(new BigDecimal("0.85"));

            // Subtract withdrawals
            BigDecimal withdrawn = withdrawalRepo.findByUserIdAndUserRole(teacher.getId(), "TEACHER")
                    .stream()
                    .filter(w -> !"REJECTED".equals(w.getStatus()))
                    .map(com.example.lms.entity.WithdrawalRequest::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            teacher.setTotalEarnings(teacherEarnings);
            teacher.setBalance(teacherEarnings.subtract(withdrawn));
            teacherRepo.save(teacher);
        }

        // 2. Sync Admins
        List<Admin> admins = adminRepo.findAll();
        BigDecimal platformRevenue = enrollmentRepo.sumTotalPaidRevenue();
        if (platformRevenue == null)
            platformRevenue = BigDecimal.ZERO;

        BigDecimal adminPlatformShare = platformRevenue.multiply(new BigDecimal("0.15"));

        for (Admin admin : admins) {
            // Subtract withdrawals
            BigDecimal withdrawn = withdrawalRepo.findByUserIdAndUserRole(admin.getId(), "ADMIN")
                    .stream()
                    .filter(w -> !"REJECTED".equals(w.getStatus()))
                    .map(com.example.lms.entity.WithdrawalRequest::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            admin.setTotalEarnings(adminPlatformShare);
            admin.setBalance(adminPlatformShare.subtract(withdrawn));
            adminRepo.save(admin);
        }
    }

    @Transactional
    public WithdrawalRequest requestWithdrawal(Authentication auth, String role, BigDecimal amount, String method) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdrawal amount must be greater than zero.");
        }

        Long userId;
        BigDecimal balance;

        if ("TEACHER".equalsIgnoreCase(role)) {
            Teacher teacher = teacherRepo.findByEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            userId = teacher.getId();
            balance = teacher.getBalance() != null ? teacher.getBalance() : BigDecimal.ZERO;
            if (balance.compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient balance. Your available balance is $" + balance);
            }
            teacher.setBalance(balance.subtract(amount));
            teacherRepo.save(teacher);
        } else if ("ADMIN".equalsIgnoreCase(role)) {
            Admin admin = adminRepo.findByEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            userId = admin.getId();
            balance = admin.getBalance() != null ? admin.getBalance() : BigDecimal.ZERO;
            if (balance.compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient balance. Your available balance is $" + balance);
            }
            admin.setBalance(balance.subtract(amount));
            adminRepo.save(admin);
        } else {
            throw new RuntimeException("Invalid role for withdrawal");
        }

        WithdrawalRequest request = new WithdrawalRequest();
        request.setUserId(userId);
        request.setUserRole(role.toUpperCase());
        request.setAmount(amount);
        request.setPaymentMethod(method);
        request.setStatus("PENDING");
        return withdrawalRepo.save(request);
    }

    public List<WithdrawalRequest> getMyWithdrawals(Authentication auth, String role) {
        Long userId;
        if ("TEACHER".equalsIgnoreCase(role)) {
            Teacher teacher = teacherRepo.findByEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            userId = teacher.getId();
        } else {
            Admin admin = adminRepo.findByEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            userId = admin.getId();
        }
        return withdrawalRepo.findByUserIdAndUserRole(userId, role.toUpperCase());
    }

    @Transactional
    public WithdrawalRequest completeWithdrawal(Long requestId, String transactionId) {
        WithdrawalRequest request = withdrawalRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("COMPLETED");
        request.setTransactionId(transactionId);
        request.setProcessedAt(LocalDateTime.now());
        return withdrawalRepo.save(request);
    }

    public List<WithdrawalRequest> getAllPendingRequests() {
        return withdrawalRepo.findByStatus("PENDING");
    }

    public List<WithdrawalRequest> getAllHistory() {
        return withdrawalRepo.findAll().stream()
                .filter(w -> !"PENDING".equals(w.getStatus()))
                .toList();
    }
}
