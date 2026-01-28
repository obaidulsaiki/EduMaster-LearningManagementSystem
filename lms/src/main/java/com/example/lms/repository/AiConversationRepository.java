package com.example.lms.repository;

import com.example.lms.entity.AiConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiConversationRepository extends JpaRepository<AiConversation, Long> {

    // Find chat history for a Student
    List<AiConversation> findByStudentId(Long studentId);

    // Find chat history for a Teacher
    List<AiConversation> findAiConversationByTeacher_Id(Long teacherId);

    // Find chat history for an Admin
    List<AiConversation> findByAdminId(Long adminId);
}
