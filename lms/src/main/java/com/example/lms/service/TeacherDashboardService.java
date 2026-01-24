package com.example.lms.service;

import com.example.lms.dto.TeacherCourseStatsDTO;
import com.example.lms.dto.TeacherDashboardDTO;
import com.example.lms.entity.Course;
import com.example.lms.entity.Teacher;
import com.example.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherDashboardService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseProgressRepository progressRepository;
    private final LectureRepository lectureRepository;

    public TeacherDashboardDTO getDashboard(Authentication auth) {

        Teacher teacher = teacherRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        List<Course> courses =
                courseRepository.findCourseByTeacher_id(teacher.getId());

        TeacherDashboardDTO dashboard = new TeacherDashboardDTO();
        dashboard.setTotalCourses(courses.size());

        int publishedCourses = 0;
        int totalStudents = 0;
        BigDecimal totalEarnings = BigDecimal.ZERO;

        List<TeacherCourseStatsDTO> courseStats = new ArrayList<>();

        for (Course course : courses) {

            if (Boolean.TRUE.equals(course.getPublished())) {
                publishedCourses++;
            }

            /* ================= STUDENTS ================= */
            long enrolledStudents =
                    enrollmentRepository.countDistinctStudentByCourseId(
                            course.getCourseId()
                    );

            /* ================= EARNINGS ================= */
            BigDecimal courseEarnings =
                    enrollmentRepository.sumPaidAmountByCourseId(
                            course.getCourseId()
                    );

            totalStudents += enrolledStudents;
            totalEarnings = totalEarnings.add(courseEarnings);

            /* ================= LECTURES ================= */
            int totalLectures =
                    lectureRepository.countByCourse_CourseId(course.getCourseId());

            /* ================= COMPLETION ================= */
            double avgCompletionR =
                    progressRepository.getAverageProgressByCourseId(
                            course.getCourseId()
                    );
            int avgCompletion = (int) avgCompletionR;
            TeacherCourseStatsDTO dto = new TeacherCourseStatsDTO();
            dto.setCourseId(course.getCourseId());
            dto.setTitle(course.getTitle());
            dto.setPrice(course.getPrice());
            dto.setPublished(course.getPublished());
            dto.setEnrolledStudents((int) enrolledStudents);
            dto.setEarnings(courseEarnings);
            dto.setTotalLectures(totalLectures);
            dto.setAvgCompletionPercentage(avgCompletion < 0 || avgCompletion > 100 ? 0 : avgCompletion);

            courseStats.add(dto);
        }

        dashboard.setPublishedCourses(publishedCourses);
        dashboard.setTotalStudents(totalStudents);
        dashboard.setTotalEarnings(totalEarnings);
        dashboard.setCourses(courseStats);

        return dashboard;
    }
}


