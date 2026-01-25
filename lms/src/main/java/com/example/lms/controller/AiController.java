package com.example.lms.controller;

import com.example.lms.dto.ChatRequestDTO;
import com.example.lms.dto.ChatResponseDTO;
import com.example.lms.service.AiIntelligenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiIntelligenceService aiIntelligenceService;

    @PostMapping("/chat")
    public ChatResponseDTO chat(@RequestBody ChatRequestDTO request) {
        return aiIntelligenceService.processChat(request);
    }
}