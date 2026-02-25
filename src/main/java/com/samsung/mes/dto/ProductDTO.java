package com.samsung.mes.dto;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private ProjectDTO project;
}