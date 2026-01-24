package com.example.lms.service;

import com.example.lms.dto.CourseProgressDTO;
import com.example.lms.dto.ResumeCourseDTO;
import com.example.lms.entity.*;
import com.example.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseProgressService {

    private final CourseProgressRepository progressRepository;
    private final CompleteLectureRepository completedLectureRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    private final CertificateRepository certificateRepository;

    /* =====================================================
                       COMPLETE LECTURE
       ===================================================== */

    @Transactional
    public void completeLecture(Authentication auth, Long lectureId) {

        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        Course course = lecture.getCourse();

        // ================= PREVENT DUPLICATE =================
        if (completedLectureRepository
                .existsCompletedLectureByStudent_IdAndLecture_Id(
                        student.getId(), lectureId)) {
            return;
        }

        // ================= SAVE COMPLETED LECTURE =================
        CompletedLecture completed = new CompletedLecture();
        completed.setStudent(student);
        completed.setLecture(lecture);
        completedLectureRepository.save(completed);

        // ================= LOAD OR CREATE PROGRESS =================
        CourseProgress progress = progressRepository
                .findByStudentIdAndCourse_CourseId(
                        student.getId(), course.getCourseId())
                .orElseGet(() -> {
                    CourseProgress cp = new CourseProgress();
                    cp.setStudent(student);
                    cp.setCourse(course);
                    cp.setCompletedLectures(0);
                    cp.setProgress(0);
                    return cp;
                });

        // ================= UPDATE LAST LECTURE =================
        progress.setLastLecture(lecture);

        // ================= RECALCULATE FROM DB =================
        int totalLectures =
                lectureRepository.countByCourse_CourseId(course.getCourseId());

        int completedLectures =
                completedLectureRepository
                        .countByStudent_IdAndLecture_Course_CourseId(
                                student.getId(), course.getCourseId());

        progress.setCompletedLectures(completedLectures);

        int percentage = (completedLectures * 100) / totalLectures;
        progress.setProgress(percentage);

        if (percentage == 100 && progress.getCompletedAt() == null) {
            progress.setCompletedAt(LocalDateTime.now());
        }

        progressRepository.save(progress);
    }


    /* =====================================================
                       RECALCULATE PROGRESS
       ===================================================== */

    private void recalculateProgress(CourseProgress progress) {

        int totalLectures =
                lectureRepository.countByCourse_CourseId(
                        progress.getCourse().getCourseId()
                );

        int completedLectures =
                completedLectureRepository.countByStudent_IdAndLecture_Course_CourseId(
                        progress.getStudent().getId(),
                        progress.getCourse().getCourseId()
                );

        int percentage = totalLectures == 0
                ? 0
                : (completedLectures * 100) / totalLectures;

        progress.setProgress(percentage);

        if (percentage == 100 && progress.getCompletedAt() == null) {
            progress.setCompletedAt(LocalDateTime.now());
        }

        if (percentage < 100) {
            progress.setCompletedAt(null);
        }
    }

    /* =====================================================
                         COURSE PROGRESS
       ===================================================== */

    public CourseProgressDTO getCourseProgress(Authentication auth, Long courseId) {

        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        CourseProgress progress = progressRepository
                .findByStudentIdAndCourse_CourseId(student.getId(), courseId)
                .orElse(null);

        CourseProgressDTO dto = new CourseProgressDTO();
        dto.setCourseId(courseId);

        if (progress == null) {
            dto.setProgress(0);
            dto.setCompleted(false);
            return dto;
        }

        dto.setProgress(progress.getProgress());
        dto.setCompleted(progress.getProgress() == 100);

        if (progress.getCompletedAt() != null) {
            dto.setCompletedAt(progress.getCompletedAt());
        }

        return dto;
    }

    /* =====================================================
                         RESUME COURSE
       ===================================================== */

    public ResumeCourseDTO getResumeData(Authentication auth, Long courseId) {

        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        CourseProgress progress = progressRepository
                .findByStudentIdAndCourse_CourseId(student.getId(), courseId)
                .orElse(null);

        ResumeCourseDTO dto = new ResumeCourseDTO();

        if (progress != null && progress.getLastLecture() != null) {
            dto.setLectureId(progress.getLastLecture().getId());
            dto.setLectureTitle(progress.getLastLecture().getTitle());
        }

        return dto;
    }

    /* =====================================================
                     COMPLETED COURSES
       ===================================================== */

    public List<CourseProgressDTO> getCompletedCourses(Authentication auth) {

        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<CourseProgress> progresses =
                progressRepository.findByStudentIdAndProgress(student.getId(), 100);

        List<CourseProgressDTO> result = new ArrayList<>();

        for (CourseProgress progress : progresses) {

            CourseProgressDTO dto = new CourseProgressDTO();
            dto.setCourseId(progress.getCourse().getCourseId());
            dto.setCourseTitle(progress.getCourse().getTitle());
            dto.setProgress(100);
            dto.setCompleted(true);
            dto.setCompletedAt(progress.getCompletedAt());

            result.add(dto);
        }

        return result;
    }
}

