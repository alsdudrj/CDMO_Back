package com.samsung.mes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString
@Getter
@Setter
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double targetQuantity;
    private String unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String version;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    @NotBlank
    @Pattern(regexp = "DRAFT|REVIEW|APPROVED")
    private String status;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Process> processes = new ArrayList<>();

    //26.02.25 민영 추가
    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Simulation simulation;
}
