package com.samsung.mes.repository;

import com.samsung.mes.entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
    // Status 필드 값이 "RUNNING"인 모든 데이터를 찾아오는 메서드
    List<Process> findAllByStatus(String status);
}
