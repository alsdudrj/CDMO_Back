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
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String name;

    @Column(name = "step_order")
    private String stepOrder;

    private String description;

    @Column(name = "temp_ph")
    private float tempPh;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate timeStamp;

    private String status;
}
