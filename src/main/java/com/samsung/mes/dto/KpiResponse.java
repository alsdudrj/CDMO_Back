package com.samsung.mes.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KpiResponse {
    private Long id;

    private String kpiName;
    private String kpiGroup;
    private String owner;

    private String periodType;
    private String periodValue;

    private BigDecimal targetValue;

    private BigDecimal actualValue;

    private String unit;
    private String status;

    private String useYn;
    private String remark;

    private LocalDateTime updatedAt;
}
