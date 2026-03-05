package com.samsung.mes.controller;

import com.samsung.mes.service.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/signatures")
@RequiredArgsConstructor
public class SignatureController {
    private final SignatureService signatureService;

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingQueue() {
        return ResponseEntity.ok(signatureService.getPendingSignatures());
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        // 실제 구현 시 SecurityContext에서 approverId를 가져오는 것이 안전합니다.
        String approverId = payload.get("approverId");
        String password = payload.get("password");

        signatureService.approveSignature(id, approverId, password);
        return ResponseEntity.ok("전자서명이 완료되었습니다.");
    }
}