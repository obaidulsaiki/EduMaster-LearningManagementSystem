package com.example.lms.controller;

import com.example.lms.entity.AssignmentSubmission;
import com.example.lms.entity.Question;
import com.example.lms.entity.Student;
import com.example.lms.repository.AssignmentSubmissionRepository;
import com.example.lms.repository.QuestionRepository;
import com.example.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentSubmissionRepository submissionRepository;
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;

    private final String UPLOAD_DIR = "uploads/assignments/";

    @PostMapping("/upload")
    public ResponseEntity<AssignmentSubmission> uploadAssignment(
            Authentication auth,
            @RequestParam("questionId") Long questionId,
            @RequestParam("file") MultipartFile file) throws IOException {

        Student student = studentRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        // Ensure directory exists
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.copy(file.getInputStream(), filePath);

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setStudent(student);
        submission.setQuestion(question);
        submission.setFileName(file.getOriginalFilename());
        submission.setFilePath(filePath.toString());
        submission.setFileType(file.getContentType());
        submission.setFileSize(file.getSize());

        return ResponseEntity.ok(submissionRepository.save(submission));
    }
}
