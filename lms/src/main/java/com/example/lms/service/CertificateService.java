package com.example.lms.service;

import com.example.lms.entity.Certificate;
import com.example.lms.entity.Course;
import com.example.lms.entity.Student;
import com.example.lms.repository.CertificateRepository;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.StudentRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfContentByte;
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
                                                courseId)
                                .orElseThrow(() -> new RuntimeException("Certificate not found"));
        }

        /* ================= AUTO GENERATE ================= */

        public void generateIfNotExists(Student student, Course course) {

                boolean exists = certificateRepository
                                .existsByStudentIdAndCourse_CourseId(
                                                student.getId(),
                                                course.getCourseId());

                if (exists)
                        return;

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
                                cert.getIssuedAt());
        }

        /* ================= PDF GENERATION ================= */

        private byte[] generateCertificatePdf(
                        String studentName,
                        String courseTitle,
                        String certificateCode,
                        LocalDateTime issuedAt) {
                try {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                        // 1. Setup Landscape Document
                        Document document = new Document(PageSize.A4.rotate(), 100, 100, 60, 60);
                        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
                        document.open();

                        PdfContentByte canvas = writer.getDirectContent();
                        float width = PageSize.A4.rotate().getWidth();
                        float height = PageSize.A4.rotate().getHeight();

                        // 2. DRAW DECORATIVE BORDER
                        // External Thick Border
                        canvas.setColorStroke(new java.awt.Color(0, 51, 102)); // Deep Blue
                        canvas.setLineWidth(15f);
                        canvas.rectangle(20, 20, width - 40, height - 40);
                        canvas.stroke();

                        // Internal Thin Gold Border
                        canvas.setColorStroke(new java.awt.Color(218, 165, 32)); // Goldenrod
                        canvas.setLineWidth(3f);
                        canvas.rectangle(40, 40, width - 80, height - 80);
                        canvas.stroke();

                        // 3. ADD CONTENT
                        // Empty space for top margin
                        document.add(new Paragraph("\n"));

                        // Header: Certificate of Completion
                        Font headerFont = new Font(Font.HELVETICA, 36, Font.BOLD, new java.awt.Color(0, 51, 102));
                        Paragraph header = new Paragraph("CERTIFICATE OF COMPLETION", headerFont);
                        header.setAlignment(Element.ALIGN_CENTER);
                        header.setSpacingBefore(20);
                        document.add(header);

                        document.add(new Paragraph("\n"));

                        // Sub-header: This is to certify that
                        Font subHeaderFont = new Font(Font.HELVETICA, 14, Font.ITALIC, java.awt.Color.DARK_GRAY);
                        Paragraph subHeader = new Paragraph("This is to certify that", subHeaderFont);
                        subHeader.setAlignment(Element.ALIGN_CENTER);
                        document.add(subHeader);

                        // Student Name
                        Font nameFont = new Font(Font.TIMES_ROMAN, 40, Font.BOLD | Font.ITALIC,
                                        new java.awt.Color(218, 165, 32));
                        Paragraph name = new Paragraph(studentName.toUpperCase(), nameFont);
                        name.setAlignment(Element.ALIGN_CENTER);
                        name.setSpacingBefore(10);
                        document.add(name);

                        // Completion Text
                        Font completionFont = new Font(Font.HELVETICA, 14, Font.NORMAL, java.awt.Color.DARK_GRAY);
                        Paragraph completionText = new Paragraph(
                                        "has successfully achieved mastery in and completed the course",
                                        completionFont);
                        completionText.setAlignment(Element.ALIGN_CENTER);
                        completionText.setSpacingBefore(10);
                        document.add(completionText);

                        // Course Title
                        Font courseFont = new Font(Font.HELVETICA, 22, Font.BOLD, new java.awt.Color(0, 51, 102));
                        Paragraph course = new Paragraph(courseTitle, courseFont);
                        course.setAlignment(Element.ALIGN_CENTER);
                        course.setSpacingBefore(10);
                        document.add(course);

                        document.add(new Paragraph("\n\n"));

                        // 4. FOOTER: CONSOLIDATED SIGNATURE AND METADATA
                        com.lowagie.text.Table footerTable = new com.lowagie.text.Table(3);
                        footerTable.setBorder(com.lowagie.text.Table.NO_BORDER);
                        footerTable.setWidth(100);
                        footerTable.setPadding(2);

                        // --- Row 1: The Signature Line ---
                        Cell emptyLeft = new Cell("");
                        emptyLeft.setBorder(Cell.NO_BORDER);
                        footerTable.addCell(emptyLeft);

                        Cell sigLineCell = new Cell("");
                        sigLineCell.setBorder(Cell.TOP); // This creates the signature line
                        sigLineCell.setBorderWidthTop(1f);
                        footerTable.addCell(sigLineCell);

                        Cell emptyRight = new Cell("");
                        emptyRight.setBorder(Cell.NO_BORDER);
                        footerTable.addCell(emptyRight);

                        // --- Row 2: Signature Title / Stamp ---
                        Font metaFont = new Font(Font.HELVETICA, 9, Font.NORMAL, java.awt.Color.GRAY);
                        Cell dateCell = new Cell(new Phrase("Issued on: " + issuedAt.toLocalDate(), metaFont));
                        dateCell.setBorder(Cell.NO_BORDER);
                        dateCell.setVerticalAlignment(Element.ALIGN_TOP);
                        footerTable.addCell(dateCell);

                        Cell sigTitleCell = new Cell(new Phrase("Authorized Program Director",
                                        new Font(Font.HELVETICA, 9, Font.NORMAL)));
                        sigTitleCell.setBorder(Cell.NO_BORDER);
                        sigTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        footerTable.addCell(sigTitleCell);

                        Cell idCell = new Cell(new Phrase("ID: " + certificateCode, metaFont));
                        idCell.setBorder(Cell.NO_BORDER);
                        idCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        footerTable.addCell(idCell);

                        // --- Row 3: Official Brand ---
                        footerTable.addCell(emptyLeft);
                        Cell brandCell = new Cell(new Phrase("EDUMASTER OFFICIAL",
                                        new Font(Font.HELVETICA, 8, Font.BOLD, new java.awt.Color(218, 165, 32))));
                        brandCell.setBorder(Cell.NO_BORDER);
                        brandCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        footerTable.addCell(brandCell);
                        footerTable.addCell(emptyRight);

                        document.add(footerTable);

                        document.close();
                        return outputStream.toByteArray();

                } catch (Exception e) {
                        throw new RuntimeException("Failed to generate certificate PDF", e);
                }
        }
}
