package com.example.lms.service;

import com.example.lms.dto.AdminDashboardStatsDTO;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.StudentRepository;
import com.example.lms.repository.TeacherRepository;
import com.example.lms.repository.EnrollmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Important for production

@Service
@AllArgsConstructor
public class AdminService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    // --- 1. Get Dashboard Statistics ---
    public AdminDashboardStatsDTO getDashboardStats() {
        AdminDashboardStatsDTO stats = new AdminDashboardStatsDTO();

        // In a real huge DB, we might use count() queries optimized by DB index
        stats.setTotalStudents(studentRepository.count());
        stats.setTotalTeachers(teacherRepository.count());
        stats.setTotalCourses(courseRepository.count());

        // Optional: You could add Total Enrollments (Revenue indicator)
        // stats.setTotalEnrollments(enrollmentRepository.count());

        return stats;
    }

    // --- 2. Delete a User (Moderation) ---
    // Using @Transactional ensures if one part fails, the whole action rolls back
    @Transactional
    public void deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }
        // Note: JPA Cascade settings in Entity will handle deleting their enrollments automatically
        // if you configured CascadeType.ALL, otherwise you delete relations first.
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void deleteTeacher(Long teacherId) {
        if (!teacherRepository.existsById(teacherId)) {
            throw new RuntimeException("Teacher not found with ID: " + teacherId);
        }
        teacherRepository.deleteById(teacherId);
    }

    // --- 3. Validate/Approve Course (Optional Workflow) ---
    public void approveCourse(Long courseId) {
        // Logic to set published = true if you had an approval workflow
        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setPublished(true);
        courseRepository.save(course);
    }
}