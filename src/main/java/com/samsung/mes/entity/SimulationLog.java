package com.samsung.mes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Table(name = "simulation_logs") // 테이블명 통일
@ToString(exclude = "simulation") // 순환 참조 방지
@Getter
@Setter
public class SimulationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simulation_id")
    @JsonIgnore
    private Simulation simulation;

    private float tempPh;
    private float phValue;
    private float doValue;
    private float progressRate;

    private LocalDateTime timeStamp;
}