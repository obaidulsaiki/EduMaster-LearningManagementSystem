package com.example.lms.controller;

import com.example.lms.dto.ChatRequestDTO;
import com.example.lms.service.AiService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@AllArgsConstructor
public class AiController {
    private final AiService aiService;

    // POST Send Message: http://localhost:8080/api/ai/chat
    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody ChatRequestDTO request) {
        try {
            return ResponseEntity.ok(aiService.processChat(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET Chat History: http://localhost:8080/api/ai/history/student/{id}
    @GetMapping("/history/student/{id}")
    public ResponseEntity<?> getHistory(@PathVariable Long id) {
        return ResponseEntity.ok(aiService.getStudentHistory(id));
    }
}