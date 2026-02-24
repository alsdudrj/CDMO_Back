package com.samsung.mes.dto;

import lombok.Data;
import java.util.List;

@Data
public class RecipeDto {
    private Long id;
    private String name;
    private String description;
    private Double targetQuantity;
    private String unit;
    private String version;
    private String status;
    private Boolean isActive;
    private Long productId;
    private List<ProcessDto> processes;
}