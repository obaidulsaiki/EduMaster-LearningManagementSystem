package com.example.lms.controller;

import com.example.lms.dto.EnrollmentStatusDTO;
import com.example.lms.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/enrollments")
@RequiredArgsConstructor
public class StudentEnrollmentController {

    private final EnrollmentService enrollmentService;

    // STEP 1: enroll intent (before payment)
    @PostMapping("/{courseId}")
    public void enroll(Authentication auth, @PathVariable Long courseId) {
        enrollmentService.createEnrollment(auth, courseId);
    }

    // STEP 2: confirm payment
    @PostMapping("/{courseId}/confirm")
    public void confirmPayment(Authentication auth, @PathVariable Long courseId) {
        enrollmentService.confirmPayment(auth, courseId);
    }

    // STEP 1 helper (used by CourseDetails.jsx)
    @GetMapping("/{courseId}/status")
    public EnrollmentStatusDTO getStatus(
            Authentication auth,
            @PathVariable Long courseId
    ) {
        return enrollmentService.getStatus(auth, courseId);
    }
}

