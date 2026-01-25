package com.example.lms.service;

import com.example.lms.dto.LectureDTO;
import com.example.lms.entity.Course;
import com.example.lms.entity.Lecture;
import com.example.lms.entity.Teacher;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.LectureRepository;
import com.example.lms.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.lms.repository.CompleteLectureRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherLectureService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    private final CompleteLectureRepository completeLectureRepository;

    private Teacher getTeacher(Authentication auth) {
        return teacherRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    public List<LectureDTO> getLectures(Authentication auth, Long courseId) {
        Teacher teacher = getTeacher(auth);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        List<LectureDTO> list = new ArrayList<>();
        for (Lecture l : course.getLectures()) {
            LectureDTO dto = new LectureDTO();
            dto.setId(l.getId());
            dto.setTitle(l.getTitle());
            dto.setVideoUrl(l.getVideoUrl());
            dto.setOrderIndex(l.getOrderIndex());
            list.add(dto);
        }
        return list;
    }

    public int getLecturesCount(Authentication auth, Long courseId) {
        Teacher teacher = getTeacher(auth);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getLectures().size();
    }

    public LectureDTO addLecture(Authentication auth, Long courseId, LectureDTO dto) {
        Teacher teacher = getTeacher(auth);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        Lecture lecture = new Lecture();
        lecture.setTitle(dto.getTitle());
        lecture.setVideoUrl(dto.getVideoUrl());
        lecture.setCourse(course);
        lecture.setOrderIndex(course.getLectures().size());

        lectureRepository.save(lecture);

        dto.setId(lecture.getId());
        return dto;
    }

    public LectureDTO updateLecture(Authentication auth, Long lectureId, LectureDTO dto) {
        Teacher teacher = getTeacher(auth);

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        if (!lecture.getCourse().getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        lecture.setTitle(dto.getTitle());
        lecture.setVideoUrl(dto.getVideoUrl());

        lectureRepository.save(lecture);

        dto.setId(lecture.getId());
        return dto;
    }

    @Transactional
    public void deleteLecture(Authentication auth, Long lectureId) {
        Teacher teacher = getTeacher(auth);

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        if (!lecture.getCourse().getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        // 1. Delete all completion records (Fixes Constraint Error)
        completeLectureRepository.deleteByLecture_Id(lectureId);

        // 2. Delete the lecture
        lectureRepository.delete(lecture);
    }

    public void reorder(Authentication auth, Long courseId, List<Long> ids) {
        Teacher teacher = getTeacher(auth);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        for (int i = 0; i < ids.size(); i++) {
            Lecture l = lectureRepository.findById(ids.get(i))
                    .orElseThrow();
            l.setOrderIndex(i);
            lectureRepository.save(l);
        }
    }
}
