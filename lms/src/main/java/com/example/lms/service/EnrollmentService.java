package com.example.lms.service;
import com.example.lms.dto.EnrollmentStatusDTO;
import com.example.lms.entity.*;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.EnrollmentRepository;
import com.example.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepo;
    private final CourseRepository courseRepo;
    private final StudentRepository studentRepo;

    public void createEnrollment(Authentication auth, Long courseId) {

        Student student = studentRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrollmentRepo.existsEnrollmentByStudent_IdAndCourse_CourseId(
                student.getId(), courseId)) return;

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setPaid(false);
        enrollment.setStatus(EnrollmentStatus.ENROLLED);

        // 🔥 CREATE PROGRESS TOGETHER
        CourseProgress progress = new CourseProgress();
        progress.setEnrollment(enrollment);
        progress.setStudent(student);
        progress.setCourse(course);
        progress.setProgress(0);
        progress.setCompletedLectures(0);
        enrollment.setCourseProgress(progress);

        enrollmentRepo.save(enrollment); // cascades progress
    }


    public void confirmPayment(Authentication auth, Long courseId) {

        Student student = studentRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Enrollment enrollment = enrollmentRepo
                .findEnrollmentByStudent_IdAndCourse_CourseId(
                        student.getId(), courseId);

        enrollment.setPaid(true);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        enrollmentRepo.save(enrollment);
    }


    public EnrollmentStatusDTO getStatus(Authentication auth, Long courseId) {

        Student student = studentRepo
                .findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Optional<Enrollment> optional =
                Optional.ofNullable(enrollmentRepo.findEnrollmentByStudent_IdAndCourse_CourseId(
                        student.getId(), courseId
                ));

        EnrollmentStatusDTO dto = new EnrollmentStatusDTO();

        if (optional.isEmpty()) {
            dto.setEnrolled(false);
            return dto;
        }

        Enrollment enrollment = optional.get();

        dto.setEnrolled(true);
        dto.setStatus(enrollment.getStatus().name());
        dto.setProgress(enrollment.getProgress());

        // 🔥 resume logic will improve later
        dto.setResumeLectureId(null);

        return dto;
    }
}

