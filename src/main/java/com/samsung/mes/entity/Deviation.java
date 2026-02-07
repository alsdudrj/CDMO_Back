package com.samsung.mes.entity;

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
public class Deviation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @Column(nullable = false)
    @NotBlank
    @Pattern(regexp = "CRITICAL|MAJOR|MINOR")
    private LocalDate severity;

    @Column(name = "is_closed", nullable = false)
    private Boolean isClosed;

    @Column(nullable = false)
    @NotBlank
    @Pattern(regexp = "OPEN|INVESTIGATING|CLOSED")
    private String status;
}
