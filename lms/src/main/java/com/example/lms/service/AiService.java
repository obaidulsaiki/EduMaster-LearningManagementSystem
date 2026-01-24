package com.example.lms.service;

import com.example.lms.dto.ChatRequestDTO;
import com.example.lms.dto.ChatResponseDTO;
import com.example.lms.entity.AiConversation;
import com.example.lms.entity.Student;
import com.example.lms.repository.AiConversationRepository;
import com.example.lms.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AiService {

    private final AiConversationRepository aiConversationRepository;
    private final StudentRepository studentRepository;

    // --- 1. Process a Chat Message ---
    public ChatResponseDTO processChat(ChatRequestDTO request) {
        // A. Validate User
        // (Assuming for now only Students chat, based on DTO)
        Student student = studentRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // B. Save the Interaction (Log it)
        AiConversation conversation = new AiConversation();
        conversation.setStudent(student);
        conversation.setStartedAt(LocalDateTime.now());
        conversation.setLastMessageSnippet(request.getMessage());
        // In real app, you'd store the whole history, not just snippet

        aiConversationRepository.save(conversation);

        // C. Generate Logic-Based Response (Mocking the LLM)
        ChatResponseDTO response = new ChatResponseDTO();

        String msg = request.getMessage().toLowerCase();

        if (msg.contains("java") || msg.contains("spring")) {
            response.setReply("It looks like you are asking about Java. I recommend checking out the 'Java Spring Boot Masterclass'.");
            response.setActionType("SUGGEST_COURSE");
            response.setActionPayload("1"); // ID of the java course
        } else if (msg.contains("my courses") || msg.contains("enrolled")) {
            response.setReply("Taking you to your enrolled courses.");
            response.setActionType("NAVIGATE");
            response.setActionPayload("/student/my-courses");
        } else {
            response.setReply("I'm an AI assistant. You can ask me about courses, your progress, or technical concepts!");
            response.setActionType("NONE");
            response.setActionPayload("");
        }

        return response;
    }

    // --- 2. Get Chat History ---
    public List<AiConversation> getStudentHistory(Long studentId) {
        return aiConversationRepository.findByStudentId(studentId);
    }
}