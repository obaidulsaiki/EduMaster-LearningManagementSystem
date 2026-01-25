package com.example.lms.controller;

import com.example.lms.dto.StudentAdminDTO;
import com.example.lms.service.AdminStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {

    private final AdminStudentService service;

    @GetMapping
    public Page<StudentAdminDTO> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search) {
        return service.getStudents(page, search);
    }

    @GetMapping("/{id}")
    public StudentAdminDTO get(@PathVariable Long id) {
        return service.getStudent(id);
    }

    @PutMapping("/{id}/toggle")
    public void toggle(@PathVariable Long id) {
        service.toggleStatus(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteStudent(id);
    }
}
