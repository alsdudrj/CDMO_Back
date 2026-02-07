package com.samsung.mes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.engine.jdbc.batch.spi.Batch;

import java.time.LocalDate;

@Entity
@ToString
@Getter
@Setter
public class QualityTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Batch batchId;

    @Column(name = "test_type", nullable = false)
    private String testType;

    @Column(nullable = false)
    private String result;

    @Column(name = "tested_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate testedAt;

    @Column(name = "tester_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tester_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member testerId;
}
