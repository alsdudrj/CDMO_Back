package com.samsung.mes.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.List;

@Data
public class RecipeDTO {
    private Long id;
    private String name;
    private String description;
    private Double targetQuantity;
    private String unit;
    private String version;

    @NotBlank
    @Pattern(regexp = "DRAFT|REVIEW|APPROVED")
    private String status;
    private Boolean isActive;
    private Long productId;
    private List<ProcessDTO> processes;
}