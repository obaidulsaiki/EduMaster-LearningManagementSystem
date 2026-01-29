package com.example.lms.service;

import com.example.lms.dto.ChatRequestDTO;
import com.example.lms.dto.ChatResponseDTO;
import com.example.lms.entity.AiConversation;
import com.example.lms.entity.Course;
import com.example.lms.entity.Student;
import com.example.lms.repository.AiConversationRepository;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.StudentRepository;
import com.example.lms.repository.StudentProfileRepository;
import com.example.lms.repository.EnrollmentRepository;
import com.example.lms.config.RoadmapData;
import com.example.lms.entity.StudentProfile;
import com.example.lms.entity.Enrollment;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiIntelligenceService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AiConversationRepository aiConversationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private LmsExpert expert;

    @PostConstruct
    public void init() {
        // Initializing local model (Ollama)
        OllamaChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("gemma3:4b")
                .build();

        this.expert = AiServices.create(LmsExpert.class, model);
    }

    public ChatResponseDTO processChat(ChatRequestDTO request) {
        // 1. Gather "Training" Context
        List<Course> courses = courseRepository.findByPublishedTrue();
        String context = courses.stream()
                .map(c -> String.format("- %s (ID: %d): %s", c.getTitle(), c.getCourseId(), c.getCategory()))
                .collect(Collectors.joining("\n"));

        String navigation = """
                Navigation Routes:
                - Public: / (Home), /browse (Browse Courses), /paths (Industry Roadmaps), /mentors (Mentors List), /login, /register
                - All Roles: /settings
                - Student: /profile, /wishlist, /course/:id (Course Details), /course/:id/enroll, /course/:id/payment, /course/:id/lecture/:id (Course Player)
                - Teacher: /teacher/dashboard, /teacher/profile, /teacher/courses
                - Admin: /admin, /admin/teachers, /admin/courses
                """;

        // 2. Gather Student Context (Bio + History)
        Student student = studentRepository.findById(request.getUserId()).orElse(null);
        String studentContext = "No student data available.";
        if (student != null) {
            String bio = studentProfileRepository.findByStudentId(student.getId())
                    .map(StudentProfile::getBio).orElse("No bio provided.");
            List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student.getId());
            String enrollmentList = enrollments.stream()
                    .map(e -> "- " + e.getCourse().getTitle())
                    .collect(Collectors.joining(", "));
            studentContext = String.format("STUDENT PROFILE:\n- Name: %s\n- Bio: %s\n- Enrolled Courses: [%s]",
                    student.getName(), bio, enrollmentList.isEmpty() ? "None yet" : enrollmentList);
        }

        // 3. Get Industry Roadmap Data
        String roadmaps = RoadmapData.getRoadmapContext();

        String finalPrompt = String.format("CONTEXT:\n%s\n\n%s\n\n%s\n\n%s\n\nUSER MESSAGE: %s",
                context, navigation, studentContext, roadmaps, request.getMessage());

        // 2. Chat with Local Model
        String aiRaw;
        try {
            aiRaw = expert.chat(finalPrompt);
        } catch (Exception e) {
            return createErrorResponse("Local AI error: " + e.getMessage());
        }

        // 3. Parse and Log
        try {
            // Find JSON block if model was talkative
            int start = aiRaw.indexOf("{");
            int end = aiRaw.lastIndexOf("}");
            if (start != -1 && end != -1) {
                aiRaw = aiRaw.substring(start, end + 1);
            }

            ChatResponseDTO response = objectMapper.readValue(aiRaw, ChatResponseDTO.class);

            // Log
            studentRepository.findById(request.getUserId()).ifPresent(s -> {
                AiConversation conversation = new AiConversation();
                conversation.setStudent(s);
                conversation.setStartedAt(LocalDateTime.now());
                conversation.setLastMessageSnippet(request.getMessage());
                aiConversationRepository.save(conversation);
            });

            return response;
        } catch (Exception e) {
            ChatResponseDTO fallback = new ChatResponseDTO();
            fallback.setReply(aiRaw);
            fallback.setActionType("NONE");
            fallback.setActionPayload("");
            return fallback;
        }
    }

    private ChatResponseDTO createErrorResponse(String msg) {
        ChatResponseDTO response = new ChatResponseDTO();
        response.setReply("⚠️ " + msg);
        response.setActionType("NONE");
        response.setActionPayload("");
        return response;
    }
}
