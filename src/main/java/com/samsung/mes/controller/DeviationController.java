package com.samsung.mes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import com.samsung.mes.dto.DeviationApprovalDTO;

import com.samsung.mes.dto.DeviationDTO;
import com.samsung.mes.service.DeviationService;
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
}
