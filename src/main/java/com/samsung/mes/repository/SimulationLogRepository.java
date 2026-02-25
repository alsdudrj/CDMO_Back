package com.samsung.mes.repository;

import com.samsung.mes.entity.SimulationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SimulationLogRepository extends JpaRepository<SimulationLog, Long> {

    // 역순 조회 (최신순 30개)
    List<SimulationLog> findTop30BySimulationIdOrderByTimeStampDesc(Long simulationId);

    //차트용 정렬 (최신 30개를 가져온 뒤, 시간 순서대로 재정렬)
    @Query(value =
            "SELECT * FROM (SELECT * FROM simulation_logs WHERE simulation_id = :simulationId ORDER BY timestamp DESC LIMIT 30) AS sub ORDER BY timestamp ASC"
            , nativeQuery = true)
    List<SimulationLog> findRecentLogs(@Param("simulationId") Long simulationId);
}