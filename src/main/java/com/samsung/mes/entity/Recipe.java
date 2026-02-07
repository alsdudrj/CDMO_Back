package com.samsung.mes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@ToString
@Getter
@Setter
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Company clientId;

    @Column(name = "contract_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate contractDate;

    @Column(nullable = false)
    private String amount;

    @Column(nullable = false)
    private String status;
}
