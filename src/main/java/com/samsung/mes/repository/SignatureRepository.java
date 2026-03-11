package com.samsung.mes.repository;

import com.samsung.mes.entity.Signature;
import org.springframework.data.jpa.repository.JpaRepository;

// 새로 생성: 실제 Signature 엔티티를 담당
public interface SignatureRepository extends JpaRepository<Signature, Long> {
}