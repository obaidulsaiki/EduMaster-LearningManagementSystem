package com.example.lms.repository;

import com.example.lms.entity.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    Optional<QuizResult> findByStudent_IdAndQuiz_Course_CourseId(Long studentId, Long courseId);

    List<QuizResult> findByQuiz_Course_CourseId(Long courseId);
}
