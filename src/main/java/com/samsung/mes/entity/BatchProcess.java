package com.samsung.mes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.engine.jdbc.batch.spi.Batch;

import java.time.LocalDate;

@Entity
@ToString
@Getter
@Setter
public class BatchProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Batch batchId;

    @Column(name = "process_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Process processId;

    @Column(name = "operator_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member operatorId;

    @Column(name = "start_time", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @Column(name = "end_time", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    @Column(nullable = false)
    @NotBlank
    @Pattern(regexp = "WAIT|DONE")
    private String status;
}
