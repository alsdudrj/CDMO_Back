package com.samsung.mes.controller;

import com.samsung.mes.entity.AuditLog;
import com.samsung.mes.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {
    private final AuditLogService auditLogService;

    @GetMapping
    public List<AuditLog> getLogs() {

        return auditLogService.getAllLogs();
    }
}