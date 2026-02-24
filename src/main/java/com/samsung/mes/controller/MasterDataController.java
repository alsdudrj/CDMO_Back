package com.samsung.mes.controller;

import com.samsung.mes.entity.Company;
import com.samsung.mes.entity.Product;
import com.samsung.mes.entity.Project;
import com.samsung.mes.repository.CompanyRepository;
import com.samsung.mes.repository.ProductRepository;
import com.samsung.mes.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow frontend access
public class MasterDataController {

    private final CompanyRepository companyRepository;
    private final ProjectRepository projectRepository;
    private final ProductRepository productRepository;

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getCompanies() {
        return ResponseEntity.ok(companyRepository.findAll());
    }

    @GetMapping("/companies/{id}/projects")
    public ResponseEntity<List<Project>> getProjectsByCompany(@PathVariable Long id) {
        return ResponseEntity.ok(projectRepository.findByCompanyId(id));
    }

    @GetMapping("/projects/{id}/products")
    public ResponseEntity<List<Product>> getProductsByProject(@PathVariable Long id) {
        return ResponseEntity.ok(productRepository.findByProjectId(id));
    }
}