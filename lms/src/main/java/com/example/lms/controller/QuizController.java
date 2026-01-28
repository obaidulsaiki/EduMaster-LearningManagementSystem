package com.example.lms.controller;

import com.example.lms.dto.QuizDTO;
import com.example.lms.dto.QuizResultResponseDTO;
import com.example.lms.dto.QuizSubmissionDTO;
import com.example.lms.entity.QuizResult;
import com.example.lms.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    // Student: Get quiz for course (without correct answers)
    @GetMapping("/course/{courseId}")
    public ResponseEntity<QuizDTO> getQuiz(@PathVariable Long courseId) {
        return ResponseEntity.ok(quizService.getQuizByCourseId(courseId, false));
    }

    // Student: Submit quiz
    @PostMapping("/submit")
    public ResponseEntity<QuizResult> submitQuiz(Authentication auth, @RequestBody QuizSubmissionDTO submission) {
        return ResponseEntity.ok(quizService.submitQuiz(auth, submission));
    }

    // Student: Get my result for course
    @GetMapping("/result/{courseId}")
    public ResponseEntity<QuizResult> getMyResult(Authentication auth, @PathVariable Long courseId) {
        return ResponseEntity.ok(quizService.getStudentResult(auth, courseId));
    }

    // Teacher: Manage quiz
    @PostMapping("/manage/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<QuizDTO> saveQuiz(@PathVariable Long courseId, @RequestBody QuizDTO quizDTO) {
        return ResponseEntity.ok(quizService.saveQuiz(courseId, quizDTO));
    }

    // Teacher: Get quiz with correct answers
    @GetMapping("/manage/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<QuizDTO> getQuizForTeacher(@PathVariable Long courseId) {
        return ResponseEntity.ok(quizService.getQuizByCourseId(courseId, true));
    }

    // Teacher: Get results for course
    @GetMapping("/results/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<QuizResultResponseDTO>> getResults(@PathVariable Long courseId) {
        return ResponseEntity.ok(quizService.getResultsByCourseId(courseId));
    }
}
