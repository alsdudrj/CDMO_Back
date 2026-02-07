package com.samsung.mes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
public class Signature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Batch batchId;

    @Column(name = "deviation_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deviation_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Deviation deviationId;

    @Column(name = "signer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signer_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member signerId;

    @Column(nullable = false)
    @NotBlank
    @Pattern(regexp = "PENDING|VERIFIED|REJECTED")
    private String status;
}
