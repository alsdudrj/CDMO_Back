package com.samsung.mes.repository;

import com.samsung.mes.entity.ProcessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProcessLogRepository extends JpaRepository<ProcessLog, Long> {

}
