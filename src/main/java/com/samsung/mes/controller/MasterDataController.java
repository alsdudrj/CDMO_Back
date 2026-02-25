package com.samsung.mes.controller;

import com.samsung.mes.dto.CompanyDTO;
import com.samsung.mes.dto.ProductDTO;
import com.samsung.mes.dto.ProjectDTO;
import com.samsung.mes.service.MasterDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend access
public class MasterDataController {

    // 해결 포인트 1: 3개의 Repository를 지우고 Service 하나만 의존성 주입을 받습니다.
    private final MasterDataService masterDataService;

    // 해결 포인트 2: 엔티티 대신 DTO를 반환하도록 Service의 메서드를 호출합니다.
    @GetMapping("/companies")
    public ResponseEntity<List<CompanyDTO>> getCompanies() {
        return ResponseEntity.ok(masterDataService.getAllCompanies());
    }

    @GetMapping("/companies/{companyId}/projects")
    public ResponseEntity<List<ProjectDTO>> getProjects(@PathVariable("companyId") Long companyId) {
        return ResponseEntity.ok(masterDataService.getProjectsByCompany(companyId));
    }

    @GetMapping("/projects/{projectId}/products")
    public ResponseEntity<List<ProductDTO>> getProducts(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(masterDataService.getProductsByProject(projectId));
    }
}
