package com.example.lms.service;

import com.example.lms.entity.Admin;
import com.example.lms.entity.AuditLog;
import com.example.lms.repository.AdminRepository;
import com.example.lms.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepo;
    private final AdminRepository adminRepo;

    public void logAction(String action, String targetType, Long targetId, String description) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Admin admin = adminRepo.findByEmail(email).orElse(null);

        AuditLog log = new AuditLog();
        if (admin != null) {
            log.setAdminId(admin.getId());
            log.setAdminName(admin.getName());
        } else {
            log.setAdminName("SYSTEM / " + email);
        }

        log.setAction(action);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setDescription(description);

        auditLogRepo.save(log);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepo.findAllByOrderByTimestampDesc();
    }
}
