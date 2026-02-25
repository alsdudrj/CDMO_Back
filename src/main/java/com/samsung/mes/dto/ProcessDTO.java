package com.samsung.mes.dto;

import lombok.Data;

@Data
public class ProcessDTO {
    private Long id;
    private String name;
    private Integer stepOrder;
    private String description;
    private Float temp;
    private Float ph;
    private String time;
}