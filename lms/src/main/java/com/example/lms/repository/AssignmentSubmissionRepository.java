package com.example.lms.repository;

import com.example.lms.entity.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {
    List<AssignmentSubmission> findByStudent_Id(Long studentId);

    List<AssignmentSubmission> findByQuestion_Id(Long questionId);
}
