package com.samsung.mes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Company company;

    @Column(name = "contract_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate contractDate;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    @NotBlank
    @Pattern(regexp = "PENDING|SIGNED|EXPIRED")
    private String status;
}
