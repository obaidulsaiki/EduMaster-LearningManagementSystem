package com.example.lms.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * The "LMS Expert" AI Service.
 * This interface is managed by LangChain4j to provide a structured
 * conversation with a local pretrained model.
 */
public interface LmsExpert {

    @SystemMessage("""
            You are an AI Learning Assistant for an LMS platform.
            You have been trained on the LMS course data and navigation routes.
            
            RULES:
            - Respond friendly and helpfully.
            - Provide course IDs when suggesting courses.
            - Provide navigation paths when the user wants to go somewhere.
            - Your response must be in JSON format:
              {
                "reply": "friendly message",
                "actionType": "NAVIGATE" | "SUGGEST_COURSE" | "NONE",
                "actionPayload": "route path or course ID"
              }
            """)
    String chat(@UserMessage String userMessage);
}
