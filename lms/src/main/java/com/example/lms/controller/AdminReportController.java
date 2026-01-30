package com.example.lms.controller;

import com.example.lms.entity.AuditLog;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.lms.dto.MonthlyReportDTO;
import com.example.lms.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService service;

    @GetMapping
    public MonthlyReportDTO getReport(@RequestParam String month) {
        return service.getReport(month);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadReport(@RequestParam String month) {
        byte[] pdf = service.generateDetailedReportPdf(month);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report-" + month + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/audit-logs")
    public List<AuditLog> getAuditLogs() {
        return service.getAllLogs();
    }

    @GetMapping("/audit-logs/csv")
    public ResponseEntity<byte[]> downloadAuditLogsCsv() {
        byte[] csv = service.generateAuditLogCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=audit-logs.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }

    @GetMapping("/revenue/csv")
    public ResponseEntity<byte[]> downloadRevenueCsv(@RequestParam String month) {
        byte[] csv = service.generateRevenueCsv(month);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=revenue-" + month + ".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}
