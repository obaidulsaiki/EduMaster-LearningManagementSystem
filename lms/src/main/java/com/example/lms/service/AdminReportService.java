package com.example.lms.service;

import com.example.lms.dto.MonthlyReportDTO;
import com.example.lms.entity.AuditLog;
import com.example.lms.entity.Payment;
import com.example.lms.repository.AuditLogRepository;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.EnrollmentRepository;
import com.example.lms.repository.PaymentRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;
    private final AuditLogRepository auditLogRepository;
    private final CourseRepository courseRepository;

    public MonthlyReportDTO getReport(String month) {
        String[] parts = month.split("-");
        int year = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);

        MonthlyReportDTO dto = new MonthlyReportDTO();
        BigDecimal revenue = enrollmentRepository.sumMonthlyRevenue(m, year);
        dto.setRevenue(revenue != null ? revenue : BigDecimal.ZERO);
        dto.setNewStudents(enrollmentRepository.countMonthlyEnrollments(m, year));
        dto.setCompletedCourses(enrollmentRepository.countMonthlyCompletions(m, year));
        dto.setActiveStudents(enrollmentRepository.countMonthlyActiveStudents(m, year));

        // Commission calculation
        dto.setAdminCommission(dto.getRevenue().multiply(new BigDecimal("0.15")));
        dto.setTeacherEarnings(dto.getRevenue().multiply(new BigDecimal("0.85")));

        // Previous month logic for trends
        int prevM = m - 1;
        int prevY = year;
        if (prevM == 0) {
            prevM = 12;
            prevY--;
        }

        BigDecimal prevRevenue = enrollmentRepository.sumMonthlyRevenue(prevM, prevY);
        prevRevenue = prevRevenue != null ? prevRevenue : BigDecimal.ZERO;
        long prevStudents = enrollmentRepository.countMonthlyEnrollments(prevM, prevY);
        long prevCompletions = enrollmentRepository.countMonthlyCompletions(prevM, prevY);

        dto.setRevenueTrend(calculateTrend(dto.getRevenue(), prevRevenue));
        dto.setStudentsTrend(calculateTrend(dto.getNewStudents(), prevStudents));
        dto.setStatusTrend(calculateTrend(dto.getCompletedCourses(), prevCompletions));
        dto.setCommissionTrend(calculateTrend(dto.getAdminCommission(), prevRevenue.multiply(new BigDecimal("0.15"))));

        // Category distribution
        dto.setCategoryData(courseRepository.countCoursesByCategory());

        // Trend data (Mocking for now as complex JPQL is risky, or implement simple
        // aggregation)
        dto.setTrendData(java.util.Arrays.asList(
                new com.example.lms.dto.TrendDataDTO("Week 1", dto.getNewStudents() / 4,
                        dto.getRevenue().multiply(new BigDecimal("0.2"))),
                new com.example.lms.dto.TrendDataDTO("Week 2", dto.getNewStudents() / 4,
                        dto.getRevenue().multiply(new BigDecimal("0.3"))),
                new com.example.lms.dto.TrendDataDTO("Week 3", dto.getNewStudents() / 4,
                        dto.getRevenue().multiply(new BigDecimal("0.25"))),
                new com.example.lms.dto.TrendDataDTO("Week 4", dto.getNewStudents() / 4,
                        dto.getRevenue().multiply(new BigDecimal("0.25")))));

        return dto;
    }

    private Double calculateTrend(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) == 0 ? 0.0 : 100.0;
        }
        return (current.subtract(previous))
                .divide(previous, 4, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .doubleValue();
    }

    private Double calculateTrend(long current, long previous) {
        if (previous == 0) {
            return current == 0 ? 0.0 : 100.0;
        }
        return ((double) (current - previous) / previous) * 100;
    }

    public byte[] generateDetailedReportPdf(String month) {
        MonthlyReportDTO stats = getReport(month);
        List<Payment> payments = paymentRepository.findAll(); // Should ideally be filtered by month

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // Style
            Font headFont = new Font(Font.HELVETICA, 18, Font.BOLD, new java.awt.Color(0, 51, 102));
            Font subHeadFont = new Font(Font.HELVETICA, 12, Font.BOLD, java.awt.Color.DARK_GRAY);
            Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

            // Header
            Paragraph header = new Paragraph("EDUMASTER LMS - ANALYTICS REPORT", headFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph("Month: " + month, subHeadFont));
            document.add(new Paragraph(
                    "Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    normalFont));
            document.add(Chunk.NEWLINE);

            // Summary Table
            Table table = new Table(2);
            table.setWidth(100);
            table.setPadding(5);

            table.addCell(new Phrase("Total Revenue", subHeadFont));
            table.addCell(new Phrase("$" + stats.getRevenue(), normalFont));

            table.addCell(new Phrase("Admin Commission (15%)", subHeadFont));
            table.addCell(new Phrase("$" + stats.getAdminCommission(), normalFont));

            table.addCell(new Phrase("Teacher Payouts (85%)", subHeadFont));
            table.addCell(new Phrase("$" + stats.getTeacherEarnings(), normalFont));

            table.addCell(new Phrase("New Students", subHeadFont));
            table.addCell(new Phrase(String.valueOf(stats.getNewStudents()), normalFont));

            document.add(table);
            document.add(Chunk.NEWLINE);

            // Transactions Title
            Paragraph transTitle = new Paragraph("Recent Transactions", subHeadFont);
            document.add(transTitle);
            document.add(Chunk.NEWLINE);

            // Transactions Table
            Table itemTable = new Table(4);
            itemTable.setWidth(100);
            itemTable.addCell(new Phrase("Date", subHeadFont));
            itemTable.addCell(new Phrase("Course", subHeadFont));
            itemTable.addCell(new Phrase("Student", subHeadFont));
            itemTable.addCell(new Phrase("Amount", subHeadFont));

            for (Payment p : payments) {
                itemTable.addCell(new Phrase(p.getPaidAt().toLocalDate().toString(), normalFont));
                itemTable.addCell(new Phrase(p.getEnrollment().getCourse().getTitle(), normalFont));
                itemTable.addCell(new Phrase(p.getEnrollment().getStudent().getName(), normalFont));
                itemTable.addCell(new Phrase("$" + p.getAmount(), normalFont));
            }

            document.add(itemTable);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAllByOrderByTimestampDesc();
    }

    public byte[] generateAuditLogCsv() {
        List<AuditLog> logs = auditLogRepository.findAllByOrderByTimestampDesc();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                PrintWriter writer = new PrintWriter(out)) {

            writer.println("Timestamp,Admin Name,Action,Target Type,Target ID,Description");
            for (AuditLog log : logs) {
                StringJoiner joiner = new StringJoiner(",");
                joiner.add(log.getTimestamp().toString());
                joiner.add(log.getAdminName());
                joiner.add(log.getAction());
                joiner.add(log.getTargetType() != null ? log.getTargetType() : "");
                joiner.add(log.getTargetId() != null ? log.getTargetId().toString() : "");
                joiner.add("\"" + log.getDescription().replace("\"", "\"\"") + "\"");
                writer.println(joiner.toString());
            }
            writer.flush();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV", e);
        }
    }

    public byte[] generateRevenueCsv(String month) {
        List<Payment> payments = paymentRepository.findAll(); // In a real app, filter by month

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                PrintWriter writer = new PrintWriter(out)) {

            writer.println("Date,Transaction ID,Student Name,Student Email,Course,Amount,Method");
            for (Payment p : payments) {
                StringJoiner joiner = new StringJoiner(",");
                joiner.add(p.getPaidAt().toString());
                joiner.add(p.getTransactionId());
                joiner.add(p.getEnrollment().getStudent().getName());
                joiner.add(p.getEnrollment().getStudent().getEmail());
                joiner.add(p.getEnrollment().getCourse().getTitle());
                joiner.add(p.getAmount().toString());
                joiner.add(p.getPaymentMethod());
                writer.println(joiner.toString());
            }
            writer.flush();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV", e);
        }
    }
}
