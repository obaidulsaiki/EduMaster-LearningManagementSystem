package com.example.lms.service;

import com.example.lms.dto.*;
import com.example.lms.entity.*;
import com.example.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final QuizResultRepository quizResultRepository;
    private final NotificationService notificationService;

    @Transactional
    public QuizDTO saveQuiz(Long courseId, QuizDTO quizDTO) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Quiz quiz = quizRepository.findByCourse_CourseId(courseId)
                .orElseGet(() -> {
                    Quiz q = new Quiz();
                    q.setCourse(course);
                    return q;
                });

        // Clear existing questions if updating
        if (quiz.getQuestions() != null) {
            quiz.getQuestions().clear();
        } else {
            quiz.setQuestions(new ArrayList<>());
        }

        for (QuestionDTO qDto : quizDTO.getQuestions()) {
            Question question = new Question();
            question.setQuestionText(qDto.getQuestionText());
            question.setOptions(qDto.getOptions());
            question.setCorrectOptionIndex(qDto.getCorrectOptionIndex());
            question.setQuiz(quiz);
            quiz.getQuestions().add(question);
        }

        Quiz savedQuiz = quizRepository.save(quiz);
        return convertToDTO(savedQuiz, true);
    }

    public QuizDTO getQuizByCourseId(Long courseId, boolean isTeacher) {
        Quiz quiz = quizRepository.findByCourse_CourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Quiz not found for this course"));

        return convertToDTO(quiz, isTeacher);
    }

    @Transactional
    public QuizResult submitQuiz(Authentication auth, QuizSubmissionDTO submission) {
        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Quiz quiz = quizRepository.findByCourse_CourseId(submission.getCourseId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        int correctCount = 0;
        Map<Long, Integer> questionToCorrectIndex = quiz.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, Question::getCorrectOptionIndex));

        for (QuizSubmissionDTO.AnswerDTO answer : submission.getAnswers()) {
            Integer correctIndex = questionToCorrectIndex.get(answer.getQuestionId());
            if (correctIndex != null && correctIndex.equals(answer.getSelectedOptionIndex())) {
                correctCount++;
            }
        }

        QuizResult result = quizResultRepository
                .findByStudent_IdAndQuiz_Course_CourseId(student.getId(), submission.getCourseId())
                .orElse(new QuizResult());

        result.setStudent(student);
        result.setQuiz(quiz);
        result.setScore(correctCount);
        result.setPassed(correctCount >= 15); // Requirement: 15/20

        QuizResult savedResult = quizResultRepository.save(result);

        // 🔥 REAL-TIME NOTIFICATION for Student
        notificationService.createNotification(
                student.getId(),
                "STUDENT",
                String.format("📝 Quiz Graded! You scored %d in the quiz for %s. Status: %s",
                        correctCount, quiz.getCourse().getTitle(), result.getPassed() ? "PASSED" : "FAILED"));

        return savedResult;
    }

    public List<QuizResultResponseDTO> getResultsByCourseId(Long courseId) {
        List<QuizResult> results = quizResultRepository.findByQuiz_Course_CourseId(courseId);
        return results.stream().map(r -> {
            QuizResultResponseDTO dto = new QuizResultResponseDTO();
            dto.setStudentName(r.getStudent().getName());
            dto.setScore(r.getScore());
            dto.setPassed(r.getPassed());
            dto.setCompletedAt(r.getCompletedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    public QuizResult getStudentResult(Authentication auth, Long courseId) {
        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return quizResultRepository.findByStudent_IdAndQuiz_Course_CourseId(student.getId(), courseId).orElse(null);
    }

    private QuizDTO convertToDTO(Quiz quiz, boolean includeCorrectIndex) {
        QuizDTO dto = new QuizDTO();
        dto.setId(quiz.getId());
        dto.setCourseId(quiz.getCourse().getCourseId());
        dto.setQuestions(quiz.getQuestions().stream().map(q -> {
            QuestionDTO qDto = new QuestionDTO();
            qDto.setId(q.getId());
            qDto.setQuestionText(q.getQuestionText());
            qDto.setOptions(q.getOptions());
            if (includeCorrectIndex) {
                qDto.setCorrectOptionIndex(q.getCorrectOptionIndex());
            }
            return qDto;
        }).collect(Collectors.toList()));
        return dto;
    }
}
