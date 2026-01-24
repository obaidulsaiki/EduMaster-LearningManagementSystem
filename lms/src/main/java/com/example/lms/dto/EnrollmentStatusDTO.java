package com.example.lms.dto;

import lombok.Data;

@Data
public class EnrollmentStatusDTO {

    private boolean enrolled;
    private String status;        // ACTIVE / PENDING_PAYMENT / COMPLETED /ENROLLED
    private int progress;     // nullable
    private Long resumeLectureId;
    private int completedLectures;
    private Long lastLectureId;
    // nullable
}