package com.samsung.mes.repository;

import com.samsung.mes.entity.ProcessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProcessLogRepository extends JpaRepository<ProcessLog, Long> {

    List<ProcessLog> findTop30ByProcessIdOrderByTimeStampDesc(Long processId);

    @Query(value =
            "SELECT * FROM (SELECT * FROM process_log WHERE process_id = :processId ORDER BY timestamp DESC LIMIT 30) AS sub ORDER BY timestamp ASC"
            , nativeQuery = true)
    List<ProcessLog> findRecentLogs(@Param("processId") Long processId);
}
