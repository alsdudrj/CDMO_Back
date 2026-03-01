package com.samsung.mes.service;

import com.samsung.mes.entity.AuditLog;
import com.samsung.mes.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public List<AuditLog> getAllLogs() {
        //최신순으로 전체 로그 조회
        return auditLogRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}