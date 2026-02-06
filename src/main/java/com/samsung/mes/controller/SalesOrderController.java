package com.samsung.mes.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.samsung.mes.dto.SalesOrderRequest;
import com.samsung.mes.dto.SalesOrderResponse;
import com.samsung.mes.service.SalesOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sales/orders")
@CrossOrigin(origins = "https://localhost:5173", allowCredentials = "true")
public class SalesOrderController {
	private final SalesOrderService service;

    // 수주 등록
    @PostMapping
    public SalesOrderResponse create(@RequestBody SalesOrderRequest req) {
        return service.create(req);
    }

    // 수주 목록 조회 (기간 + 페이징)
    @GetMapping
    public Page<SalesOrderResponse> list(
            @RequestParam(name = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(name = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,

            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return service.list(from, to, page, size);
    }

    // 수주 상세
    @GetMapping("/{id}")
    public SalesOrderResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    // 수주 수정
    @PutMapping("/{id}")
    public SalesOrderResponse update(@PathVariable Long id, @RequestBody SalesOrderRequest req) {
        return service.update(id, req);
    }

    // 수주 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
