package com.example.lms.service;

import com.example.lms.entity.Notification;
import com.example.lms.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 1. Create Notification (Call this from EnrollmentService or CourseService!)
    public void createNotification(Long recipientId, String role, String message) {
        Notification notif = new Notification();
        notif.setRecipientId(recipientId);
        notif.setRecipientRole(role);
        notif.setMessage(message);
        notificationRepository.save(notif);
    }

    // 2. Get User Notifications
    public List<Notification> getUserNotifications(Long userId, String role) {
        return notificationRepository.findByRecipientIdAndRecipientRoleOrderByCreatedAtDesc(userId, role);
    }

    // 3. Mark as Read
    public void markAsRead(Long notificationId) {
        if(notificationRepository.existsById(notificationId)) {
            Notification n = notificationRepository.findById(notificationId).get();
            n.setIsRead(true);
            notificationRepository.save(n);
        }
    }
}