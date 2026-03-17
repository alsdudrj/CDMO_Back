package com.samsung.mes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviationDTO {
    private Long id;
    private String batchId;
    private String parameter;
    private Double recordedValue;
    private Double limitValue;
    private String severity;
    private String status;
    private Boolean isClosed;
    private LocalDateTime createdAt;
}
