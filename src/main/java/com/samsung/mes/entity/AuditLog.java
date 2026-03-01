package com.samsung.mes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entityName;
    private Long entityId;
    private String action;          //CREATE, UPDATE, DELETE
    private String username;        //작업자

    @Column(columnDefinition = "TEXT")
    private String beforeValue;     //JSON

    @Column(columnDefinition = "TEXT")
    private String afterValue;      //JSON

    private LocalDateTime createdAt;
}
