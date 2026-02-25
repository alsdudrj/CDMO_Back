package com.samsung.mes.controller;

import com.samsung.mes.entity.ProcessLog;
import com.samsung.mes.entity.SimulationLog;
import com.samsung.mes.repository.ProcessLogRepository;
import com.samsung.mes.repository.SimulationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:5173", allowCredentials = "true")
public class SimulationController {

    private final SimulationLogRepository simulationLogRepository;

    @GetMapping("/api/simulation/{id}/logs")
    public List<SimulationLog> getRecentLogs(@PathVariable Long id) {
        // 최근 30개의 데이터를 시간순으로 가져오는 Repository 메서드 호출
        return simulationLogRepository.findTop30BySimulationIdOrderByTimeStampDesc(id);
    }
}
