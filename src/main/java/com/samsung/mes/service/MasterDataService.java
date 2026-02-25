package com.samsung.mes.service;

import com.samsung.mes.dto.CompanyDTO;
import com.samsung.mes.dto.ProductDTO;
import com.samsung.mes.dto.ProjectDTO;
import com.samsung.mes.repository.CompanyRepository;
import com.samsung.mes.repository.ProductRepository;
import com.samsung.mes.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MasterDataService {

    private final CompanyRepository companyRepository;
    private final ProjectRepository projectRepository;
    private final ProductRepository productRepository;

    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream().map(c -> {
            CompanyDTO dto = new CompanyDTO();
            dto.setId(c.getId());
            dto.setName(c.getName());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<ProjectDTO> getProjectsByCompany(Long companyId) {
        // 엔티티 필드명에 따라 Repository 메서드명이 바뀌면 여기도 맞춰서 수정해주세요.
        return projectRepository.findByCompanyId(companyId).stream().map(p -> {
            ProjectDTO dto = new ProjectDTO();
            dto.setId(p.getId());
            dto.setName(p.getName());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByProject(Long projectId) {
        return productRepository.findByProjectId(projectId).stream().map(p -> {
            ProductDTO dto = new ProductDTO();
            dto.setId(p.getId());
            dto.setName(p.getName());
            return dto;
        }).collect(Collectors.toList());
    }
}