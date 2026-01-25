package com.example.lms.service;

import com.example.lms.dto.*;
import com.example.lms.entity.Course;
import com.example.lms.entity.Lecture;
import com.example.lms.entity.Teacher;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.LectureRepository;
import com.example.lms.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final LectureRepository lectureRepository;

    /* ===================== HELPERS ===================== */

    public CourseDTO getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        CourseDTO dto = new CourseDTO();
        dto.setCourseId(course.getCourseId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setPrice(course.getPrice());
        dto.setPublished(course.getPublished());
        dto.setCategory(course.getCategory());

        if (course.getTeacher() != null) {
            dto.setTeacherId(course.getTeacher().getId());
            dto.setTeacherName(course.getTeacher().getName());
        }

        dto.setLectureCount(lectureRepository.countByCourse_CourseId(courseId));

        return dto;
    }

    private Teacher getTeacher(Authentication auth) {
        return teacherRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    public List<String> getAllCategories() {
        return courseRepository.findDistinctCategories();
    }

    private Course getOwnedCourse(Long courseId, Teacher teacher) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
        return course;
    }

    /* ===================== COURSES ===================== */

    public CourseDTO createCourse(Authentication auth, CourseDTO dto) {
        Teacher teacher = getTeacher(auth);

        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setPrice(dto.getPrice());
        course.setCategory(dto.getCategory());
        course.setPublished(false);
        course.setEnabled(true);
        course.setTeacher(teacher);

        courseRepository.save(course);
        return toCourseDTO(course);
    }

    public List<CourseDTO> getMyCourses(Authentication auth) {
        Teacher teacher = getTeacher(auth);

        List<Course> courses = courseRepository.findCourseByTeacher_id(teacher.getId());

        List<CourseDTO> result = new ArrayList<>();

        for (Course course : courses) {
            result.add(toCourseDTO(course));
        }
        return result;
    }

    public CourseDTO updateCourse(
            Authentication auth,
            Long courseId,
            CourseDTO dto) {
        Teacher teacher = getTeacher(auth);
        Course course = getOwnedCourse(courseId, teacher);

        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setPrice(dto.getPrice());
        course.setCategory(dto.getCategory());
        course.setPublished(dto.getPublished());

        courseRepository.save(course);
        return toCourseDTO(course);
    }

    public void deleteCourse(Authentication auth, Long courseId) {
        Teacher teacher = getTeacher(auth);
        Course course = getOwnedCourse(courseId, teacher);

        List<Lecture> lectures = (List<Lecture>) lectureRepository.findByCourse_CourseIdOrderByOrderIndexAsc(courseId);

        for (Lecture l : lectures) {
            lectureRepository.delete(l);
        }

        courseRepository.delete(course);
    }

    /* ===================== LECTURES ===================== */

    public List<LectureDTO> getLecturesByCourse(
            Authentication auth,
            Long courseId) {
        Teacher teacher = getTeacher(auth);
        getOwnedCourse(courseId, teacher);

        List<Lecture> lectures = (List<Lecture>) lectureRepository.findByCourse_CourseIdOrderByOrderIndexAsc(courseId);

        List<LectureDTO> result = new ArrayList<>();

        for (Lecture lecture : lectures) {
            result.add(toLectureDTO(lecture));
        }
        return result;
    }

    public LectureDTO addLecture(
            Authentication auth,
            Long courseId,
            LectureDTO dto) {
        Teacher teacher = getTeacher(auth);
        Course course = getOwnedCourse(courseId, teacher);

        List<Lecture> maxOrder = lectureRepository.findByCourse_CourseIdOrderByOrderIndexAsc(courseId);

        int maxOrderValue = maxOrder.isEmpty() ? 0 : maxOrder.get(maxOrder.size() - 1).getOrderIndex();

        Lecture lecture = new Lecture();
        lecture.setTitle(dto.getTitle());
        lecture.setVideoUrl(dto.getVideoUrl());
        lecture.setOrderIndex(maxOrderValue + 1);
        lecture.setCourse(course);
        lectureRepository.save(lecture);
        return toLectureDTO(lecture);
    }

    public LectureDTO updateLecture(
            Authentication auth,
            Long lectureId,
            LectureDTO dto) {
        Teacher teacher = getTeacher(auth);

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        if (!lecture.getCourse().getTeacher().getId()
                .equals(teacher.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        lecture.setTitle(dto.getTitle());
        lecture.setVideoUrl(dto.getVideoUrl());

        lectureRepository.save(lecture);
        return toLectureDTO(lecture);
    }

    public void deleteLecture(Authentication auth, Long lectureId) {
        Teacher teacher = getTeacher(auth);

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        if (!lecture.getCourse().getTeacher().getId()
                .equals(teacher.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        lectureRepository.delete(lecture);
    }

    public void reorderLectures(
            Authentication auth,
            Long courseId,
            List<Long> lectureIds) {
        Teacher teacher = getTeacher(auth);
        getOwnedCourse(courseId, teacher);

        int order = 1;
        for (Long id : lectureIds) {
            Lecture lecture = lectureRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Lecture not found"));

            lecture.setOrderIndex(order++);
            lectureRepository.save(lecture);
        }
    }

    /* ===================== PUBLIC BROWSING ===================== */

    public CoursePageResponseDTO getFilteredCourses(
            int page,
            int size,
            String category,
            Double minPrice,
            Double maxPrice,
            String sort,
            String search) {
        Sort sorting = Sort.unsorted();

        if (sort != null && sort.contains(",")) {
            String[] parts = sort.split(",");
            sorting = Sort.by(
                    "desc".equalsIgnoreCase(parts[1])
                            ? Sort.Direction.DESC
                            : Sort.Direction.ASC,
                    parts[0]);
        }

        Pageable pageable = PageRequest.of(page, size, sorting);

        Specification<Course> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isTrue(root.get("published")));

            if (category != null)
                predicates.add(cb.equal(root.get("category"), category));

            if (minPrice != null)
                predicates.add(cb.ge(root.get("price"), minPrice));

            if (maxPrice != null)
                predicates.add(cb.le(root.get("price"), maxPrice));

            if (search != null && !search.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + search.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Course> pageResult = courseRepository.findAll(spec, pageable);

        List<CourseDTO> courses = new ArrayList<>();
        for (Course c : pageResult.getContent()) {
            courses.add(toCourseDTO(c));
        }

        CoursePageResponseDTO res = new CoursePageResponseDTO();
        res.setCourses(courses);
        res.setCurrentPage(pageResult.getNumber());
        res.setTotalPages(pageResult.getTotalPages());
        res.setTotalElements(pageResult.getTotalElements());

        return res;
    }

    /* ===================== MAPPERS ===================== */

    private CourseDTO toCourseDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setCourseId(course.getCourseId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setPrice(course.getPrice());
        dto.setCategory(course.getCategory());
        dto.setPublished(course.getPublished());
        dto.setTeacherId(course.getTeacher().getId());
        dto.setTeacherName(course.getTeacher().getName());
        dto.setLectureCount(lectureRepository.countByCourse_CourseId(course.getCourseId()));
        return dto;
    }

    private LectureDTO toLectureDTO(Lecture lecture) {
        LectureDTO dto = new LectureDTO();
        dto.setId(lecture.getId());
        dto.setTitle(lecture.getTitle());
        dto.setVideoUrl(lecture.getVideoUrl());
        dto.setOrderIndex(lecture.getOrderIndex());
        return dto;
    }

    public CourseDetailsDTO getCourseDetails(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // 🔒 Students can only see published courses
        if (!course.getPublished()) {
            throw new RuntimeException("Course not published");
        }

        // ===== LECTURES =====
        List<Lecture> lectureEntities = lectureRepository.findByCourse_CourseId(courseId);

        List<LectureResponseDTO> lectureDTOs = new ArrayList<>();
        for (Lecture lecture : lectureEntities) {
            LectureResponseDTO lectureDTO = new LectureResponseDTO();
            lectureDTO.setId(lecture.getId());
            lectureDTO.setTitle(lecture.getTitle());
            lectureDTO.setVideoUrl(lecture.getVideoUrl());
            lectureDTO.setOrderIndex(lecture.getOrderIndex());
            lectureDTOs.add(lectureDTO);
        }
        int lectureCount = lectureDTOs.size();

        // ===== TEACHER =====
        Teacher teacher = course.getTeacher();

        // ===== COURSE DTO =====
        CourseDetailsDTO dto = new CourseDetailsDTO();
        dto.setCourseId(course.getCourseId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setPrice(course.getPrice());
        dto.setCategory(course.getCategory());
        dto.setPublished(course.getPublished());

        // teacher info
        dto.setTeacherId(teacher.getId());
        dto.setTeacherName(teacher.getName());
        dto.setTeacherEmail(teacher.getEmail());

        // lectures
        dto.setLectureCount(lectureCount);
        dto.setLectures(lectureDTOs);

        return dto;
    }

}
