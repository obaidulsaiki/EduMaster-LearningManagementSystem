package com.example.lms.repository;

import com.example.lms.entity.CompletedLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompleteLectureRepository extends JpaRepository<CompletedLecture, Long> {
    boolean existsCompletedLectureByStudent_IdAndLecture_Id(Long studentId, Long lectureId);

    int countByStudent_IdAndLecture_Course_CourseId(Long id, Long courseId);

    void deleteByLecture_Id(Long lectureId);
}
