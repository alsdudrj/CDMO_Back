package com.samsung.mes.repository;

import com.samsung.mes.entity.Deviation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface DeviationRepository extends JpaRepository<Deviation, Long>, JpaSpecificationExecutor<Deviation> {

}
