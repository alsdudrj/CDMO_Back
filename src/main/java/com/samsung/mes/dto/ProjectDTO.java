package com.samsung.mes.dto;
import lombok.Data;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private CompanyDTO company;
}