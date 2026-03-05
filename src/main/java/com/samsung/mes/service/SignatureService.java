package com.samsung.mes.service;

import com.samsung.mes.dto.SignatureRequest;
import com.samsung.mes.entity.User;
import com.samsung.mes.repository.SignatureRepository;
import com.samsung.mes.repository.UserRepository;
import com.samsung.mes.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignatureService {
    private final SignatureRepository signatureRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService; // 이전에 말씀드린 공통 CRUD 이력 저장 엔진

    // 대기열 목록 불러오기
    public List<SignatureRequest> getPendingSignatures() {
        return signatureRepository.findByStatusOrderByCreatedAtDesc("PENDING");
    }

    // 전자서명 승인 처리 (트랜잭션 적용)
    @Transactional
    public void approveSignature(Long requestId, String approverId, String password) {
        // 1. 관리자 비밀번호 검증 (전자서명의 핵심)
        User approver = userRepository.findById(approverId).orElseThrow();
        if (!approver.checkPassword(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 2. 대기열 상태 업데이트
        SignatureRequest request = signatureRepository.findById(requestId).orElseThrow();
        request.setStatus("APPROVED");
        signatureRepository.save(request);

        // 3. 실제 원본 데이터(레시피 등) 상태 변경 로직 호출 (생략)

        // 4. Audit Log 기록 (필수)
        auditLogService.logAction(approverId, "SIGNATURE_APPROVED", request.getTargetType(), request.getTargetId());
    }
}