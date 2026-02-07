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
public class deviation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Batch batchId;

    @Column(nullable = false)
    private LocalDate severity;

    @Column(name = "is_closed", nullable = false)
    private String isClosed;

    @Column(nullable = false)
    private String status;
}
