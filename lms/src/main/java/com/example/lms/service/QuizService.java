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
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

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
            question.setType(
                    qDto.getType() != null ? QuestionType.valueOf(qDto.getType()) : QuestionType.MULTIPLE_CHOICE);
            question.setExtraData(qDto.getExtraData());
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
        boolean hasFileSubmission = false;

        Map<Long, Question> questionMap = quiz.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        for (QuizSubmissionDTO.AnswerDTO answer : submission.getAnswers()) {
            Question q = questionMap.get(answer.getQuestionId());
            if (q == null)
                continue;

            switch (q.getType()) {
                case MULTIPLE_CHOICE:
                    if (q.getCorrectOptionIndex() != null
                            && q.getCorrectOptionIndex().equals(answer.getSelectedOptionIndex())) {
                        correctCount++;
                    }
                    break;
                case MATCHING:
                    if (checkMatchingAnswer(q, answer.getMatchingResponse())) {
                        correctCount++;
                    }
                    break;
                case FILL_IN_THE_BLANKS:
                    if (checkBlanksAnswer(q, answer.getTextResponse())) {
                        correctCount++;
                    }
                    break;
                case FILE_SUBMISSION:
                    hasFileSubmission = true;
                    // File submission ID is logged; actual file handling happens in separate upload
                    break;
            }
        }

        QuizResult result = quizResultRepository
                .findByStudent_IdAndQuiz_Course_CourseId(student.getId(), submission.getCourseId())
                .orElse(new QuizResult());

        result.setStudent(student);
        result.setQuiz(quiz);
        result.setScore(correctCount);

        // Passing threshold: 75%
        double percentage = (double) correctCount / quiz.getQuestions().size();
        result.setPassed(percentage >= 0.75);
        result.setStatus(hasFileSubmission ? QuizStatus.PENDING_REVIEW : QuizStatus.COMPLETED);

        QuizResult savedResult = quizResultRepository.save(result);

        String statusMsg = result.getStatus() == QuizStatus.PENDING_REVIEW ? "PENDING REVIEW"
                : (result.getPassed() ? "PASSED" : "FAILED");
        notificationService.createNotification(
                student.getId(),
                "STUDENT",
                String.format("📝 Quiz Submitted! You scored %d in %s. Status: %s",
                        correctCount, quiz.getCourse().getTitle(), statusMsg));

        return savedResult;
    }

    private boolean checkMatchingAnswer(Question q, Map<String, String> response) {
        if (response == null || q.getExtraData() == null)
            return false;
        try {
            Map<String, Object> data = objectMapper.readValue(q.getExtraData(), Map.class);
            Map<String, String> correctPairs = (Map<String, String>) data.get("pairs");
            return correctPairs != null && correctPairs.equals(response);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkBlanksAnswer(Question q, String response) {
        if (response == null || q.getExtraData() == null)
            return false;
        try {
            Map<String, Object> data = objectMapper.readValue(q.getExtraData(), Map.class);
            List<String> correctAnswers = (List<String>) data.get("answers");
            if (correctAnswers == null)
                return false;
            String[] studentAnswers = response.split(",");
            if (studentAnswers.length != correctAnswers.size())
                return false;
            for (int i = 0; i < correctAnswers.size(); i++) {
                if (!correctAnswers.get(i).trim().equalsIgnoreCase(studentAnswers[i].trim()))
                    return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
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
            qDto.setType(q.getType().name());
            qDto.setExtraData(q.getExtraData());
            qDto.setOptions(q.getOptions());
            if (includeCorrectIndex) {
                qDto.setCorrectOptionIndex(q.getCorrectOptionIndex());
            }
            return qDto;
        }).collect(Collectors.toList()));
        return dto;
    }
}
