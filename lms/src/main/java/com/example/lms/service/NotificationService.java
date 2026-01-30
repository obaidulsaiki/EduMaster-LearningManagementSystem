package com.example.lms.service;

import com.example.lms.entity.Notification;
import com.example.lms.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // 1. Create Notification (Call this from EnrollmentService or CourseService!)
    public void createNotification(Long recipientId, String role, String message) {
        Notification notif = new Notification();
        notif.setRecipientId(recipientId);
        notif.setRecipientRole(role);
        notif.setMessage(message);
        notificationRepository.save(notif);

        // Real-time broadcast: /topic/notifications/{role}-{userId}
        String destination = String.format("/topic/notifications/%s-%d", role, recipientId);
        messagingTemplate.convertAndSend(destination, notif);
    }

    // 2. Get User Notifications
    public List<Notification> getUserNotifications(Long userId, String role) {
        return notificationRepository.findByRecipientIdAndRecipientRoleOrderByCreatedAtDesc(userId, role);
    }

    // 3. Mark as Read
    public void markAsRead(Long notificationId) {
        if (notificationRepository.existsById(notificationId)) {
            Notification n = notificationRepository.findById(notificationId).get();
            n.setIsRead(true);
            notificationRepository.save(n);
        }
    }
}