package com.samsung.mes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "kpi", uniqueConstraints = { @UniqueConstraint(
            name = "uk_kpi_name_period", columnNames = {"kpi_name", "period_type", "status"}
        )
    }, indexes = { @Index(name = "ix_kpi_name", columnList = "kpi_name"),
                    @Index(name = "ix_kpi_owner", columnList = "owner"),
    }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Kpi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member clientId;

    @Column(name = "yield_rate", nullable = false)
    private Float yieldRate;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    ////////////////////////////////////////////////////////

    @Column(name = "kpi_name", length = 200, nullable = false)
    @NotBlank
    private String kpiName;

    @Column(name = "kpi_group", length = 100)
    private String kpiGroup;

    @Column(name = "owner", length = 100)
    private String owner;

    @Column(name = "period_type", length = 10, nullable = false)
    @NotBlank
    @Pattern(regexp = "MONTH|QUARTER|YEAR")
    private String periodType;

    @Column(name = "period_value", length = 20, nullable = false)
    @NotBlank
    private String periodValue;

    @Column(name = "target_value", precision = 18, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal targetValue;

    @Column(name = "actual_value", precision = 18, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal actualValue;

    @Column(name = "unit", length = 30)
    private String unit;

    @Column(name = "status", length = 20, nullable = false)
    @NotBlank
    @Pattern(regexp = "ON_TRACK|RISK|OFF_TRACK")
    private String status;

    @Column(name = "use_yn", length = 1, nullable = false)
    @Pattern(regexp = "Y|N")
    private String useYn;

    @Column(name = "remark", length = 500)
    private String remark;

    @PrePersist
    public void prePersist(){ //엔티티가 저장/수정될 때 자동으로 처리해야 할 공통 규칙을 한 곳에 모아두는 곳
        if(targetValue == null) { targetValue = BigDecimal.ZERO; }
        if(actualValue == null) { actualValue = BigDecimal.ZERO; }

        if(status == null || status.isBlank()) { status = "ON_TRACK"; }
        if(useYn == null || useYn.isBlank()) { useYn = "Y"; }

        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
