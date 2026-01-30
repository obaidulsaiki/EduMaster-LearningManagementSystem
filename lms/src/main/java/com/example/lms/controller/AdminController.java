package com.example.lms.controller;

import com.example.lms.service.AdminService;
import com.example.lms.service.AuditLogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final AuditLogService auditLogService;

    // DELETE Ban Student: http://localhost:8080/api/admin/student/{id}
    @DeleteMapping("/student/{id}")
    public ResponseEntity<?> banStudent(@PathVariable Long id) {
        try {
            adminService.deleteStudent(id);
            auditLogService.logAction("BAN_STUDENT", "STUDENT", id, "Admin banned student with ID: " + id);
            return ResponseEntity.ok("Student banned/deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE Ban Teacher
    @DeleteMapping("/teacher/{id}")
    public ResponseEntity<?> banTeacher(@PathVariable Long id) {
        try {
            adminService.deleteTeacher(id);
            auditLogService.logAction("BAN_TEACHER", "TEACHER", id, "Admin banned teacher with ID: " + id);
            return ResponseEntity.ok("Teacher banned/deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}