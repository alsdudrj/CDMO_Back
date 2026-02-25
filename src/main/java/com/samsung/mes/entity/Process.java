package com.samsung.mes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이 공정이 어떤 레시피에 속하는지 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    private String name;

    @Column(name = "step_order")
    private Integer stepOrder;  // 지원: drop down 위해서 integer로 수정
    private String description;

    @Column(name = "temp")
    private Float temp;       // 온도 저장용

    @Column(name = "progress_rate")
    private Float progressRate; // 진행률 저장용

    @Column(name = "ph")
    private Float ph;      // 수소이온농도

    @Column(name = "do_value")
    private Float doValue;      // 용존 산소량

    @Column(name = "time")
    private String time;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeStamp;

    private String status;
}
