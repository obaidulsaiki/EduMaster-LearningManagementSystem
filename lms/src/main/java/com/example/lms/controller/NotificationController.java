package com.example.lms.controller;

import com.example.lms.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // GET My Notifications: http://localhost:8080/api/notifications/user/{id}?role=STUDENT
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserNotifications(@PathVariable Long id, @RequestParam String role) {
        return ResponseEntity.ok(notificationService.getUserNotifications(id, role));
    }

    // POST Mark as Read: http://localhost:8080/api/notifications/read/{id}
    @PostMapping("/read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Marked as read");
    }
}