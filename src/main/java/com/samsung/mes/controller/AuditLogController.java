package com.samsung.mes.controller;

import com.samsung.mes.entity.AuditLog;
import com.samsung.mes.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {
    private final AuditLogService auditLogService;

//    @GetMapping
//    public List<AuditLog> getLogs() {
//
//        return auditLogService.getAllLogs();
//    }

    //검색 추가
    @GetMapping
    public Page<AuditLog> searchLogs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end = endDate != null ? endDate.atTime(23,59,59) : null;

        return auditLogService.searchLogs(
                keyword,
                action,
                start,
                end,
                page,
                size
        );
    }
}