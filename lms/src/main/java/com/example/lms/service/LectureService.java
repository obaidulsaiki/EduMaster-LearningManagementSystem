package com.example.lms.service;

import com.example.lms.dto.LectureRequestDTO;
import com.example.lms.dto.LectureResponseDTO;
import com.example.lms.entity.Course;
import com.example.lms.entity.Lecture;
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
public class LectureService {

    private final LectureRepository lectureRepository;
    private final CourseRepository courseRepository;

    /* ================= STUDENT ================= */

    public List<LectureResponseDTO> getLecturesByCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<Lecture> lectures =
                lectureRepository.findByCourse_CourseId(course.getCourseId());

        List<LectureResponseDTO> result = new ArrayList<>();

        for (Lecture lecture : lectures) {
            result.add(toDto(lecture));
        }

        return result;
    }

    public LectureResponseDTO getLecture(Long lectureId) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        return toDto(lecture);
    }

    private LectureResponseDTO toDto(Lecture lecture) {

        LectureResponseDTO dto = new LectureResponseDTO();
        dto.setId(lecture.getId());
        dto.setTitle(lecture.getTitle());
        dto.setVideoUrl(lecture.getVideoUrl());
        dto.setOrderIndex(lecture.getOrderIndex());
        return dto;
    }
}
