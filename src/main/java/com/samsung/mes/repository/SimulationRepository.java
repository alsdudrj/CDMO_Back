package com.samsung.mes.repository;

import com.samsung.mes.entity.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, Long> {
    // "RUNNING" 상태인 시뮬레이션 목록 조회
    List<Simulation> findAllByStatus(String status);
}