package com.example.lms.controller;

import com.example.lms.dto.MonthlyReportDTO;
import com.example.lms.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService service;

    @GetMapping
    public MonthlyReportDTO getReport(@RequestParam String month) {
        return service.getReport(month);
    }
}
