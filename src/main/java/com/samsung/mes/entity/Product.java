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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Project projectId;

    @Column(nullable = false)
    private String name;

    @Column(name = "dosage_form", nullable = false)
    private String dosageForm;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String strangth;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
