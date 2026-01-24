package com.example.lms.service;

import com.example.lms.dto.CourseDTO;
import com.example.lms.dto.TeacherCourseRequestDTO;
import com.example.lms.dto.TeacherCourseResponseDTO;
import com.example.lms.entity.Course;
import com.example.lms.entity.Teacher;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.LectureRepository;
import com.example.lms.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherCourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final LectureRepository lectureRepository;

    /* ================= CREATE ================= */
    public TeacherCourseResponseDTO createCourse(
            Authentication auth,
            TeacherCourseRequestDTO dto
    ) {
        Teacher teacher = getTeacher(auth);

        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setCategory(dto.getCategory());
        course.setPrice(dto.getPrice());
        course.setPublished(dto.getPublished() != null && dto.getPublished());
        course.setTeacher(teacher);

        Course saved = courseRepository.save(course);
        return toDto(saved);
    }

    /* ================= GET MY COURSES ================= */
    public List<CourseDTO> getMyCourses(Authentication auth) {
        Teacher teacher = teacherRepository
                .findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        List<Course> courses = courseRepository.findCourseByTeacher_id(teacher.getId());
        List<CourseDTO> result = new ArrayList<>();

        for (Course course : courses) {
            CourseDTO dto = new CourseDTO();
            dto.setCourseId(course.getCourseId());
            dto.setTitle(course.getTitle());
            dto.setDescription(course.getDescription());
            dto.setPrice(course.getPrice());
            dto.setCategory(course.getCategory());
            dto.setPublished(course.getPublished());

            // 🔥 REAL COUNT FROM DB
            int lectureCount =
                    lectureRepository.countByCourse_CourseId(course.getCourseId());

            dto.setLectureCount(lectureCount);

            result.add(dto);
        }

        return result;
    }


    /* ================= UPDATE ================= */
    public TeacherCourseResponseDTO updateCourse(
            Authentication auth,
            Long courseId,
            TeacherCourseRequestDTO dto
    ) {
        Course course = getOwnedCourse(auth, courseId);

        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setCategory(dto.getCategory());
        course.setPrice(dto.getPrice());
        course.setPublished(dto.getPublished());

        return toDto(courseRepository.save(course));
    }

    /* ================= DELETE ================= */
    public void deleteCourse(Authentication auth, Long courseId) {
        Course course = getOwnedCourse(auth, courseId);
        courseRepository.delete(course);
    }

    /* ================= HELPERS ================= */

    private Teacher getTeacher(Authentication auth) {
        return teacherRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    private Course getOwnedCourse(Authentication auth, Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getTeacher().getEmail().equals(auth.getName())) {
            throw new RuntimeException("Unauthorized access");
        }

        return course;
    }

    private TeacherCourseResponseDTO toDto(Course course) {

        TeacherCourseResponseDTO dto = new TeacherCourseResponseDTO();
        dto.setCourseId(course.getCourseId());
        dto.setTitle(course.getTitle());
        dto.setCategory(course.getCategory());
        dto.setPrice(course.getPrice());
        dto.setPublished(course.getPublished());

        dto.setLectureCount(
                course.getLectures() == null ? 0 : course.getLectures().size()
        );

        dto.setEnrolledStudents(
                course.getCourseProgress() == null ? 0 : course.getCourseProgress().size()
        );

        return dto;
    }
    public void publishCourse(Authentication auth, Long courseId) {
        Course course = getOwnedCourse(auth, courseId);
        course.setPublished(true);
        courseRepository.save(course);
    }

    public void unpublishCourse(Authentication auth, Long courseId) {
        Course course = getOwnedCourse(auth, courseId);
        course.setPublished(false);
        courseRepository.save(course);
    }

}


