package com.example.lms.controller;

import com.example.lms.dto.CourseAdminDTO;
import com.example.lms.service.AdminCourseService;
import com.example.lms.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final AdminCourseService service;
    private final AuditLogService auditLogService;

    @GetMapping
    public Page<CourseAdminDTO> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search) {
        return service.getCourses(page, search);
    }

    @GetMapping("/{id}")
    public CourseAdminDTO get(@PathVariable Long id) {
        return service.getCourse(id);
    }

    @PutMapping("/{id}/toggle")
    public void toggle(@PathVariable Long id) {
        service.toggleStatus(id);
        auditLogService.logAction("TOGGLE_COURSE_STATUS", "COURSE", id, "Admin toggled status for course ID: " + id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteCourse(id);
        auditLogService.logAction("DELETE_COURSE", "COURSE", id, "Admin deleted course ID: " + id);
    }

    @PutMapping("/{id}/publish")
    public void togglePublish(@PathVariable Long id) {
        service.togglePublish(id);
        auditLogService.logAction("TOGGLE_COURSE_PUBLISH", "COURSE", id,
                "Admin toggled publish status for course ID: " + id);
    }
}
