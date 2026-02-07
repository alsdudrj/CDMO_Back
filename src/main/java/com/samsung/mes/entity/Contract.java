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
public class GMPDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Project projectId;

    @Column(nullable = false)
    private String type;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}
