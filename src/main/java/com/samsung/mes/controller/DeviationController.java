package com.samsung.mes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import com.samsung.mes.dto.DeviationApprovalDTO;

import com.samsung.mes.dto.DeviationDTO;
import com.samsung.mes.service.DeviationService;

import java.time.LocalDate;
//import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/deviations")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DeviationController {

    private final DeviationService deviationService;

    @Autowired
    public DeviationController(DeviationService deviationService) {

        this.deviationService = deviationService;
    }
    // simulateDeviation 메서드
    @GetMapping("/simulate")
    public DeviationDTO simulateDeviation() {
        return deviationService.simulateDeviation();
    }

    // 코어 연동: 일탈 승인 및 전자서명 처리
    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveDeviation(
            @PathVariable Long id,
            @RequestBody DeviationApprovalDTO request) {

        deviationService.approveDeviationWithSignature(id, request);
        return ResponseEntity.ok("일탈 승인 및 전자서명이 성공적으로 완료되었습니다.");
    }
    
    //(26.03.16 민영추가) 검색기능
    @GetMapping("/search")
    public Page<DeviationDTO> search(

            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate,
            Pageable pageable
    ){
        keyword = (keyword != null && keyword.isBlank()) ? null : keyword;
        severity = (severity != null && severity.isBlank()) ? null : severity;
        status = (status != null && status.isBlank()) ? null : status;

        return deviationService.searchDeviations(
                severity,
                status,
                keyword,
                startDate,
                endDate,
                pageable
        );
    }
}
