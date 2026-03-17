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

    // ✨ 추가 1: 어떤 레시피(또는 프로세스)에서 발생한 일탈인지 추적
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    // ✨ 추가 2: 추후 Audit을 위해 DTO에 있던 데이터를 DB로 끌어내림
    @Column(nullable = false)
    private String parameter;      // 예: "Temperature", "pH", "Dissolved Oxygen"
    @Column(nullable = false)
    private Double limitValue;     // 레시피(Process)에 설정된 기준값
    @Column(nullable = false)
    private Double recordedValue;  // 실제 측정된 일탈 수치

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
