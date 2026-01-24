package com.example.lms.controller;

import com.example.lms.entity.Certificate;
import com.example.lms.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    // ================= GET CERTIFICATE INFO =================
    @GetMapping("/{courseId}")
    public Certificate getMyCertificate(
            Authentication authentication,
            @PathVariable Long courseId
    ) {
        return certificateService.getMyCertificate(authentication, courseId);
    }

    // ================= DOWNLOAD PDF =================
    @GetMapping("/{courseId}/download")
    public ResponseEntity<byte[]> downloadCertificate(
            Authentication authentication,
            @PathVariable Long courseId
    ) {

        byte[] pdf = certificateService
                .downloadCertificate(authentication, courseId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=certificate.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
