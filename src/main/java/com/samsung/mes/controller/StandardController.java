package com.samsung.mes.controller;

import com.samsung.mes.dto.StandardRequest;
import com.samsung.mes.dto.StandardResponse;
import com.samsung.mes.service.StandardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/standards")
public class StandardController {
    private final StandardService service;

    @GetMapping
    public Page<StandardResponse> list(
            @RequestParam(required = false) String useYn,
            @RequestParam(required = false, defaultValue = "NAME") String searchType,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return service.list(useYn, searchType, keyword, pageable);
    }

    @GetMapping("/{id}")
    public StandardResponse getOne(@PathVariable Long id){
        return service.getOne(id);
    }

    @PostMapping
    public StandardResponse create(@Valid @RequestBody StandardRequest req){
        return service.create(req);
    }

    @PutMapping("/{id}")
    public StandardResponse update(@PathVariable Long id, @Valid @RequestBody StandardRequest req){
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

    @PatchMapping("/{id}/disable")
    public StandardResponse disable(@PathVariable Long id) { return service.disable(id); }
}
