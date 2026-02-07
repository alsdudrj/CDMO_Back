package com.samsung.mes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @NotBlank
    @Pattern(regexp = "API|EXCIPIENT")
    private String type;

    @Column(name = "supplier_company_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_company_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Company supplierCompanyId;

    @Column(nullable = false)
    private String spec;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private double minStock;
}
