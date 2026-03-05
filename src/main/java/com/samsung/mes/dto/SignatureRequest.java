package com.samsung.mes.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "signature_request")
@Data
public class SignatureRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String targetType;
    private Long targetId;
    private String requesterId;
    private String status; // PENDING, APPROVED, REJECTED
    private LocalDateTime createdAt;
}