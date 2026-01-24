package com.example.lms.controller;

import com.example.lms.dto.EducationDTO;
import com.example.lms.entity.Education;
import com.example.lms.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/education")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    /* ================= CREATE ================= */
    @PostMapping
    public Education create(@RequestBody EducationDTO dto) {
        return educationService.createEducation(dto);
    }

    /* ================= READ ================= */
    @GetMapping
    public List<Education> getMyEducations() {
        return educationService.getMyEducations();
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public Education update(
            @PathVariable Long id,
            @RequestBody EducationDTO dto
    ) {
        return educationService.updateEducation(id, dto);
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        educationService.deleteEducation(id);
    }
}
