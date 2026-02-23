package com.samsung.mes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore // JSON 변환 시 process 정보는 제외 (순환 참조 방지)
    private Process process; // FK (어떤 공정의 데이터인지)

    private float tempPh;
    private float phValue;
    private float doValue;
    private float progressRate;

    private LocalDateTime timeStamp; // 기록 시간
}
