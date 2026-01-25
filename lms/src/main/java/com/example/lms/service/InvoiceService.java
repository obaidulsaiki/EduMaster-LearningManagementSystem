package com.example.lms.service;

import com.example.lms.entity.Course;
import com.example.lms.entity.Student;
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
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public byte[] generateInvoicePdf(Authentication auth, Long courseId) {
        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            // --- HEADER: LOGO & INVOICE TITLE ---
            Font titleFont = new Font(Font.HELVETICA, 24, Font.BOLD, new java.awt.Color(0, 51, 102));
            Paragraph title = new Paragraph("INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_RIGHT);
            document.add(title);

            Font brandFont = new Font(Font.HELVETICA, 18, Font.BOLD, new java.awt.Color(218, 165, 32));
            Paragraph brand = new Paragraph("EDUMASTER LMS", brandFont);
            brand.setAlignment(Element.ALIGN_LEFT);
            document.add(brand);

            document.add(new Paragraph("\n"));

            // --- INFO TABLE ---
            com.lowagie.text.Table infoTable = new com.lowagie.text.Table(2);
            infoTable.setBorder(com.lowagie.text.Table.NO_BORDER);
            infoTable.setWidth(100);

            Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font valueFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

            // Left: Bill To
            Cell billTo = new Cell(new Phrase("BILL TO:\n", labelFont));
            billTo.add(new Phrase(student.getName() + "\n", valueFont));
            billTo.add(new Phrase(student.getEmail(), valueFont));
            billTo.setBorder(Cell.NO_BORDER);
            infoTable.addCell(billTo);

            // Right: Invoice Info
            Cell invInfo = new Cell(new Phrase("INVOICE #: ", labelFont));
            invInfo.add(new Phrase(UUID.randomUUID().toString().substring(0, 8).toUpperCase() + "\n", valueFont));
            invInfo.add(new Phrase("DATE: ", labelFont));
            invInfo.add(new Phrase(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n",
                    valueFont));
            invInfo.setBorder(Cell.NO_BORDER);
            invInfo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            infoTable.addCell(invInfo);

            document.add(infoTable);
            document.add(new Paragraph("\n\n"));

            // --- ITEMS TABLE ---
            com.lowagie.text.Table itemTable = new com.lowagie.text.Table(2);
            itemTable.setWidth(100);
            itemTable.setPadding(5);
            itemTable.setBorderWidth(1);
            itemTable.setCellsFitPage(true);

            Cell h1 = new Cell(new Phrase("Description", labelFont));
            h1.setBackgroundColor(new java.awt.Color(240, 240, 240));
            itemTable.addCell(h1);

            Cell h2 = new Cell(new Phrase("Amount", labelFont));
            h2.setBackgroundColor(new java.awt.Color(240, 240, 240));
            h2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            itemTable.addCell(h2);

            itemTable.addCell(new Phrase("Course Enrollment: " + course.getTitle(), valueFont));
            Cell priceCell = new Cell(new Phrase("$" + course.getPrice(), valueFont));
            priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            itemTable.addCell(priceCell);

            document.add(itemTable);

            // --- TOTAL ---
            Paragraph total = new Paragraph("\nTOTAL PAID: $" + course.getPrice(),
                    new Font(Font.HELVETICA, 14, Font.BOLD, new java.awt.Color(0, 102, 0)));
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.add(new Paragraph("\n\n\n"));
            Paragraph footer = new Paragraph("Thank you for choosing EduMaster! Your learning journey starts now.",
                    new Font(Font.HELVETICA, 10, Font.ITALIC, java.awt.Color.GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Invoice PDF", e);
        }
    }
}
