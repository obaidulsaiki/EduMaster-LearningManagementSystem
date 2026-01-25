package com.example.lms.repository;

import com.example.lms.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
  // Get all courses a specific student owns (for "My Courses" page)
  List<Enrollment> findByStudentId(Long studentId);

  boolean existsEnrollmentByStudent_IdAndCourse_CourseId(Long studentId, Long courseId);

  Enrollment findEnrollmentByStudent_IdAndCourse_CourseId(Long studentId, Long courseId);

  Optional<Object> findByStudent_IdAndCourse_CourseId(Long studentId, Long courseCourseId);

  @Query("""
          SELECT COUNT(DISTINCT e.student.id)
          FROM Enrollment e
          WHERE e.course.courseId = :courseId
      """)
  long countDistinctStudentByCourseId(@Param("courseId") Long courseId);

  /* ================= EARNINGS ================= */
  @Query("""
          SELECT COALESCE(SUM(e.course.price), 0)
          FROM Enrollment e
          WHERE e.course.courseId = :courseId
            AND e.isPaid = true
      """)
  BigDecimal sumPaidAmountByCourseId(@Param("courseId") Long courseId);

  @Query("""
        select coalesce(sum(e.course.price), 0)
        from Enrollment e
        where e.isPaid = true
      """)
  BigDecimal sumTotalPaidRevenue();

  @Query("""
          SELECT COALESCE(SUM(e.course.price), 0)
          FROM Enrollment e
          WHERE e.isPaid = true
            AND MONTH(e.enrolledAt) = :month
            AND YEAR(e.enrolledAt) = :year
      """)
  BigDecimal sumMonthlyRevenue(int month, int year);

  @Query("""
          SELECT COUNT(e)
          FROM Enrollment e
          WHERE MONTH(e.enrolledAt) = :month
            AND YEAR(e.enrolledAt) = :year
      """)
  long countMonthlyEnrollments(int month, int year);

  @Query("""
          SELECT COUNT(e)
          FROM Enrollment e
          WHERE e.completedAt IS NOT NULL
            AND MONTH(e.completedAt) = :month
            AND YEAR(e.completedAt) = :year
      """)
  long countMonthlyCompletions(int month, int year);

  @Query("""
          SELECT COUNT(DISTINCT e.student.id)
          FROM Enrollment e
          WHERE MONTH(e.enrolledAt) = :month
            AND YEAR(e.enrolledAt) = :year
      """)
  long countMonthlyActiveStudents(int month, int year);
}