package com.samsung.mes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@Table(name = "deviation", indexes = {
        @Index(name = "idx_deviation_status_severity", columnList = "status, severity")
})
public class Deviation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @Column(nullable = false)
    private String parameter;

    @Column(nullable = false)
    private Double recordedValue;

    @Column(nullable = false)
    private Double limitValue;

    @Column(nullable = false)
    @NotBlank
    @Pattern(regexp = "CRITICAL|MAJOR|MINOR")
    private String severity;

    @Column(nullable = false)
    @NotBlank
    @Pattern(regexp = "OPEN|INVESTIGATING|CLOSED")
    private String status;

    @Column(name = "is_closed", nullable = false)
    private Boolean isClosed = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
