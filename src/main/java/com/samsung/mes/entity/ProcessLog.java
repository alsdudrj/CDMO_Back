package com.samsung.mes.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
public class ProcessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id")
    private Process process; // FK (어떤 공정의 데이터인지)

    private Integer progressRate; // 진행률 (0~100)

    private String logMessage; // 로그 메시지

    private LocalDateTime createdAt; // 기록 시간
}
