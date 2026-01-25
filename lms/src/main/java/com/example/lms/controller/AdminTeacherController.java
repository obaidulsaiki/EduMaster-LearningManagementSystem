package com.example.lms.controller;

import com.example.lms.dto.TeacherAdminDTO;
import com.example.lms.service.AdminTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/teachers")
@RequiredArgsConstructor
public class AdminTeacherController {

    private final AdminTeacherService service;

    @GetMapping
    public Page<TeacherAdminDTO> getTeachers(
            @RequestParam int page,
            @RequestParam(required = false) String search) {
        return service.getTeachers(page, search);
    }

    @GetMapping("/{id}")
    public TeacherAdminDTO getTeacher(@PathVariable Long id) {
        return service.getTeacher(id);
    }

    @PutMapping("/{id}")
    public TeacherAdminDTO update(
            @PathVariable Long id,
            @RequestBody TeacherAdminDTO dto) {
        return service.updateTeacher(id, dto);
    }

    @PutMapping("/{id}/toggle")
    public void toggle(@PathVariable Long id) {
        service.toggleStatus(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteTeacher(id);
    }
}
