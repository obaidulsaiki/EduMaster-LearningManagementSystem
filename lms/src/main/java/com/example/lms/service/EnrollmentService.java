package com.example.lms.service;

import com.example.lms.dto.EnrollmentStatusDTO;
import com.example.lms.entity.*;
import com.example.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

        private final EnrollmentRepository enrollmentRepo;
        private final CourseRepository courseRepo;
        private final StudentRepository studentRepo;
        private final PaymentRepository paymentRepo;
        private final TeacherRepository teacherRepo;
        private final AdminRepository adminRepo;

        public void createEnrollment(Authentication auth, Long courseId) {

                Student student = studentRepo.findByEmail(auth.getName())
                                .orElseThrow(() -> new RuntimeException("Student not found"));

                Course course = courseRepo.findById(courseId)
                                .orElseThrow(() -> new RuntimeException("Course not found"));

                if (enrollmentRepo.existsEnrollmentByStudent_IdAndCourse_CourseId(
                                student.getId(), courseId))
                        return;

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

        @Transactional
        public void confirmPayment(Authentication auth, Long courseId, com.example.lms.dto.PaymentRequestDTO req) {

                Student student = studentRepo.findByEmail(auth.getName())
                                .orElseThrow(() -> new RuntimeException("Student not found"));

                Enrollment enrollment = enrollmentRepo
                                .findEnrollmentByStudent_IdAndCourse_CourseId(
                                                student.getId(), courseId);

                if (enrollment == null)
                        throw new RuntimeException("Enrollment not found");

                enrollment.setPaid(true);
                enrollment.setStatus(EnrollmentStatus.ACTIVE);

                // CREATE PAYMENT RECORD
                Payment payment = new Payment();
                payment.setEnrollment(enrollment);
                payment.setTransactionId(req.getTransactionId());
                payment.setPaymentMethod(req.getMethod());
                payment.setAmount(req.getAmount() != null ? req.getAmount() : enrollment.getCourse().getPrice());
                payment.setPaidAt(LocalDateTime.now());
                payment.setSslStatus("VALID");

                paymentRepo.save(payment);
                enrollmentRepo.save(enrollment);

                // 🔥 REVENUE SPLIT (85% Teacher, 15% Admin)
                java.math.BigDecimal amount = payment.getAmount();
                java.math.BigDecimal teacherShare = amount.multiply(new java.math.BigDecimal("0.85"));
                java.math.BigDecimal adminShare = amount.multiply(new java.math.BigDecimal("0.15"));

                // Update Teacher Balance
                Teacher teacher = enrollment.getCourse().getTeacher();
                java.math.BigDecimal currentTeacherBalance = teacher.getBalance() != null ? teacher.getBalance()
                                : java.math.BigDecimal.ZERO;
                java.math.BigDecimal currentTeacherTotal = teacher.getTotalEarnings() != null
                                ? teacher.getTotalEarnings()
                                : java.math.BigDecimal.ZERO;

                teacher.setBalance(currentTeacherBalance.add(teacherShare));
                teacher.setTotalEarnings(currentTeacherTotal.add(teacherShare));
                teacherRepo.save(teacher);

                // Update Admin Balance (Primary Admin)
                Admin admin = adminRepo.findAll().stream().findFirst().orElse(null);
                if (admin != null) {
                        java.math.BigDecimal currentAdminBalance = admin.getBalance() != null ? admin.getBalance()
                                        : java.math.BigDecimal.ZERO;
                        java.math.BigDecimal currentAdminTotal = admin.getTotalEarnings() != null
                                        ? admin.getTotalEarnings()
                                        : java.math.BigDecimal.ZERO;

                        admin.setBalance(currentAdminBalance.add(adminShare));
                        admin.setTotalEarnings(currentAdminTotal.add(adminShare));
                        adminRepo.save(admin);
                }
        }

        public EnrollmentStatusDTO getStatus(Authentication auth, Long courseId) {

                Student student = studentRepo
                                .findByEmail(auth.getName())
                                .orElseThrow(() -> new RuntimeException("Student not found"));

                Optional<Enrollment> optional = Optional
                                .ofNullable(enrollmentRepo.findEnrollmentByStudent_IdAndCourse_CourseId(
                                                student.getId(), courseId));

                EnrollmentStatusDTO dto = new EnrollmentStatusDTO();

                if (optional.isEmpty()) {
                        dto.setEnrolled(false);
                        return dto;
                }

                Enrollment enrollment = optional.get();

                dto.setEnrolled(true);
                dto.setStatus(enrollment.getStatus().name());

                if (enrollment.getCourseProgress() != null) {
                        dto.setProgress(enrollment.getCourseProgress().getProgress());
                        if (enrollment.getCourseProgress().getLastLecture() != null) {
                                dto.setResumeLectureId(enrollment.getCourseProgress().getLastLecture().getId());
                        }
                } else {
                        dto.setProgress(0);
                        dto.setResumeLectureId(null);
                }

                return dto;
        }
}
