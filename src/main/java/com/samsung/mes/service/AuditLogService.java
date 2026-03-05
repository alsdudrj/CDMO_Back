package com.samsung.mes.service;

import com.samsung.mes.entity.AuditLog;
import com.samsung.mes.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public List<AuditLog> getAllLogs() {
// 지원: 최신순(가장 최근에 생성된 로그가 먼저 나오도록)으로 조회하기 위해 ASC에서 DESC로 수정
        return auditLogRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional
    public void logAction(String username, String action, String entityName, Long entityId) {
        // @Builder 패턴을 사용하여 객체 생성 코드를 직관적이고 깔끔하게 작성
        AuditLog auditLog = AuditLog.builder()
                .username(username)
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .createdAt(LocalDateTime.now())
                // 참고: 단순 서명 승인 로그일 경우 before/after value는 생략하거나 null로 둡니다.
                .build();

        auditLogRepository.save(auditLog);
    }
}