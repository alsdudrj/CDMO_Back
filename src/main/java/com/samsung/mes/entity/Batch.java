package com.samsung.mes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
public class batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String batchNo;

    @Column(nullable = false)
    private String quantity;

    @Column(nullable = false)
    private String status;

    @Column(name = "mfg_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate mfgDate;

    @Column(name = "exp_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
