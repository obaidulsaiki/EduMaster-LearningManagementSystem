package com.example.lms.controller;

import com.example.lms.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/{courseId}/download")
    public ResponseEntity<byte[]> downloadInvoice(
            Authentication authentication,
            @PathVariable Long courseId) {
        byte[] pdf = invoiceService.generateInvoicePdf(authentication, courseId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
