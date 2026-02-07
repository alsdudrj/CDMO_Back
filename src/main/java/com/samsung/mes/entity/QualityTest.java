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
public class QualityTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @Column(name = "test_type", nullable = false)
    private String testType;

    @Column(nullable = false)
    @NotBlank
    @Pattern(regexp = "PASS|FAIL")
    private String result;

    @Column(name = "tested_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate testedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tester_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member testerId;
}
