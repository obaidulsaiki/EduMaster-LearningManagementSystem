package com.example.lms.repository;

import com.example.lms.entity.WithdrawalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WithdrawalRequestRepository extends JpaRepository<WithdrawalRequest, Long> {
    List<WithdrawalRequest> findByUserIdAndUserRole(Long userId, String userRole);

    List<WithdrawalRequest> findByStatus(String status);
}
