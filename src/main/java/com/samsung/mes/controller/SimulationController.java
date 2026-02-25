package com.samsung.mes.controller;

import com.samsung.mes.entity.ProcessLog;
import com.samsung.mes.entity.Simulation;
import com.samsung.mes.entity.SimulationLog;
import com.samsung.mes.repository.ProcessLogRepository;
import com.samsung.mes.repository.SimulationLogRepository;
import com.samsung.mes.repository.SimulationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/simulation")
public class SimulationController {

    private final SimulationRepository simulationRepository;
    private final SimulationLogRepository simulationLogRepository;

    @GetMapping("/{id}/logs")
    public List<SimulationLog> getRecentLogs(@PathVariable Long id) {
        // 최근 30개의 데이터를 시간순으로 가져오는 Repository 메서드 호출
        return simulationLogRepository.findTop30BySimulationIdOrderByTimeStampDesc(id);
    }

    @GetMapping("/by-recipe/{recipeId}")
    public Simulation getByRecipe(@PathVariable Long recipeId) {
        return simulationRepository.findByRecipeId(recipeId)
                .orElseThrow(() -> new RuntimeException("Simulation not found"));
    }
}
