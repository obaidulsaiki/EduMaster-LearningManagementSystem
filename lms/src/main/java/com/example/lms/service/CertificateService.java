package com.example.lms.service;

import com.example.lms.entity.Certificate;
import com.example.lms.entity.Course;
import com.example.lms.entity.Student;
import com.example.lms.repository.CertificateRepository;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.StudentRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    /* ================= INTERNAL HELPERS ================= */

    private Student getStudent(Authentication auth) {
        return studentRepository
                .findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    private Course getCourse(Long courseId) {
        return courseRepository
                .findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    /* ================= GET CERTIFICATE ENTITY ================= */

    public Certificate getMyCertificate(Authentication auth, Long courseId) {

        Student student = getStudent(auth);

        return certificateRepository
                .findByStudentIdAndCourse_CourseId(
                        student.getId(),
                        courseId
                )
                .orElseThrow(() ->
                        new RuntimeException("Certificate not found"));
    }

    /* ================= AUTO GENERATE ================= */

    public void generateIfNotExists(Student student, Course course) {

        boolean exists = certificateRepository
                .existsByStudentIdAndCourse_CourseId(
                        student.getId(),
                        course.getCourseId()
                );

        if (exists) return;

        Certificate cert = new Certificate();
        cert.setStudent(student);
        cert.setCourse(course);
        cert.setIssuedAt(LocalDateTime.now());
        cert.setCertificateCode(UUID.randomUUID().toString());

        certificateRepository.save(cert);
    }

    /* ================= DOWNLOAD PDF ================= */

    public byte[] downloadCertificate(Authentication auth, Long courseId) {

        Student student = getStudent(auth);
        Course course = getCourse(courseId);

        // ensure certificate exists
        generateIfNotExists(student, course);

        Certificate cert = getMyCertificate(auth, courseId);

        return generateCertificatePdf(
                student.getName(),
                course.getTitle(),
                cert.getCertificateCode(),
                cert.getIssuedAt()
        );
    }

    /* ================= PDF GENERATION ================= */

    private byte[] generateCertificatePdf(
            String studentName,
            String courseTitle,
            String certificateCode,
            LocalDateTime issuedAt
    ) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);

            document.open();

            Font titleFont = new Font(Font.HELVETICA, 26, Font.BOLD);
            Paragraph title = new Paragraph("Certificate of Completion", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n\n"));

            Font bodyFont = new Font(Font.HELVETICA, 14);
            Paragraph body = new Paragraph(
                    "This is to certify that\n\n" +
                            studentName + "\n\n" +
                            "has successfully completed the course\n\n" +
                            courseTitle,
                    bodyFont
            );
            body.setAlignment(Element.ALIGN_CENTER);
            document.add(body);

            document.add(new Paragraph("\n\n"));

            Font footerFont = new Font(Font.HELVETICA, 10);
            Paragraph footer = new Paragraph(
                    "Issued on: " + issuedAt.toLocalDate() + "\n" +
                            "Certificate Code: " + certificateCode,
                    footerFont
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate certificate PDF", e);
        }
    }
}
