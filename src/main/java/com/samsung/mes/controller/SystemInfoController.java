package com.samsung.mes.controller;

import com.samsung.mes.dto.SystemInfoRequest;
import com.samsung.mes.dto.SystemInfoResponse;
import com.samsung.mes.service.SystemInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/systems")
public class SystemInfoController {
    private final SystemInfoService service;

    @GetMapping
    public Page<SystemInfoResponse> list(
            @RequestParam(required = false) String useYn,
            @RequestParam(required = false, defaultValue = "NAME") String searchType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return service.list(useYn, searchType, keyword, status, pageable);
    }

    @GetMapping("/{id}")
    public SystemInfoResponse getOne(@PathVariable Long id){
        return service.getOne(id);
    }

    @PostMapping
    public SystemInfoResponse create(@Valid @RequestBody SystemInfoRequest req){
        return service.create(req);
    }

    @PutMapping("/{id}")
    public SystemInfoResponse update(@PathVariable Long id, @Valid @RequestBody SystemInfoRequest req){
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

    @PatchMapping("/{id}/disable")
    public SystemInfoResponse disable(@PathVariable Long id) {
        return service.disable(id);
    }
}
