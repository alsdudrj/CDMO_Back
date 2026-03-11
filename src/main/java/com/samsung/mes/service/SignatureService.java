package com.samsung.mes.service;

import com.samsung.mes.dto.SignatureRequest;
import com.samsung.mes.entity.User;
import com.samsung.mes.repository.SignatureRepository;
import com.samsung.mes.repository.SignatureRequestRepository;
import com.samsung.mes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import com.samsung.mes.entity.Deviation;
import com.samsung.mes.entity.Signature;
import com.samsung.mes.dto.DeviationApprovalDTO;

@Service
@RequiredArgsConstructor
public class SignatureService {

    // 분리한 두 개의 Repository를 각각 주입받습니다.
    private final SignatureRequestRepository signatureRequestRepository;
    private final SignatureRepository signatureRepository;

    private final UserRepository userRepository;
    private final AuditLogService auditLogService; // 공통 CRUD 이력 저장 엔진

    // 대기열 목록 불러오기 (SignatureRequest 다룸)
    public List<SignatureRequest> getPendingSignatures() {
        return signatureRequestRepository.findByStatusOrderByCreatedAtDesc("PENDING");
    }

    // 전자서명 승인 처리 (트랜잭션 적용)
    @Transactional
    public void createSignatureForDeviation(Deviation deviation, DeviationApprovalDTO request) {
        // 1. 서명자 확인 및 비밀번호 검증 (기존 validate 로직 활용)
        User approver = userRepository.findById(request.getApproverId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!approver.checkPassword(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 2. Signature 엔티티 생성 및 ERD 1:1 매핑
        Signature signature = new Signature();
        signature.setDeviation(deviation);
        signature.setBatch(deviation.getBatch());
        // signature.setMember(approver); // Member-User 관계에 맞게 설정 필요
        signature.setStatus("VERIFIED"); // 승인 완료 상태

        // Signature 타입 전용 Repository이므로 에러 없이 정상 저장됩니다.
        signatureRepository.save(signature);

        // 3. 공통 Audit Log 기록
        auditLogService.logAction(request.getApproverId(), "DEVIATION_APPROVED", "DEVIATION", deviation.getId());
    }

    // 전자서명 승인 처리 (대기열 상태 업데이트)
    @Transactional
    public void approveSignature(Long requestId, String approverId, String password) {
        // 1. 서명자 확인 및 비밀번호 검증
        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!approver.checkPassword(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 2. 대기열 상태 업데이트 (분리한 SignatureRequestRepository 사용)
        SignatureRequest request = signatureRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("해당 서명 요청을 찾을 수 없습니다."));

        request.setStatus("APPROVED");
        signatureRequestRepository.save(request);

        // 3. 실제 원본 데이터 상태 변경 로직 (필요 시 추가)

        // 4. Audit Log 기록
        auditLogService.logAction(approverId, "SIGNATURE_APPROVED", request.getTargetType(), request.getTargetId());
    }
}