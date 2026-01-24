package com.example.lms.service;

import com.example.lms.dto.MonthlyReportDTO;
import com.example.lms.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final EnrollmentRepository enrollmentRepository;

    public MonthlyReportDTO getReport(String month) {

        String[] parts = month.split("-");
        int year = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);

        MonthlyReportDTO dto = new MonthlyReportDTO();
        dto.setRevenue(enrollmentRepository.sumTotalPaidRevenue());
        dto.setNewStudents(enrollmentRepository.countMonthlyEnrollments(m, year));
        dto.setCompletedCourses(enrollmentRepository.countMonthlyCompletions(m, year));

        return dto;
    }
}

