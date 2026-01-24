package com.example.lms.service;

import com.example.lms.dto.AdminDashboardDTO;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.EnrollmentRepository;
import com.example.lms.repository.StudentRepository;
import com.example.lms.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AdminDashboardDTO getDashboard() {

        AdminDashboardDTO dto = new AdminDashboardDTO();

        dto.setTotalTeachers(teacherRepository.count());
        dto.setTotalStudents(studentRepository.count());
        dto.setTotalCourses(courseRepository.count());

        BigDecimal revenue =
                enrollmentRepository.sumTotalPaidRevenue();

        dto.setTotalRevenue(revenue == null ? BigDecimal.ZERO : revenue);

        return dto;
    }
}

