package com.samsung.mes.dto;

import lombok.Data;

@Data
public class SimulationDTO {
    private Long id;
    private String name;
    private Integer stepOrder;
    private String description;
    private Float temp;
    private Float ph;
    private Float doValue;
    private Float progressRate;
    private String time;
}