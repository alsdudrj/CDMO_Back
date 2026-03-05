package com.samsung.mes.repository;

import com.samsung.mes.dto.SignatureRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignatureRepository extends JpaRepository<SignatureRequest, Long> {
    // 관리자가 볼 대기열 (PENDING 상태인 것만 조회)
    List<SignatureRequest> findByStatusOrderByCreatedAtDesc(String status);
}