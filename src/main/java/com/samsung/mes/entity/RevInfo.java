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
public class RevInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rev;

    @Column(name = "rev_tstmp", nullable = false)
    private LocalDateTime revTstmp;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_ip", nullable = false)
    private String userIp;

    @Column(name = "change_reason", nullable = false)
    private String changeReason;
}
