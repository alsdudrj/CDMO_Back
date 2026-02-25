package com.samsung.mes.service;

import com.samsung.mes.entity.Simulation;
import com.samsung.mes.entity.SimulationLog;
import com.samsung.mes.repository.SimulationLogRepository;
import com.samsung.mes.repository.SimulationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final SimulationLogRepository simulationLogRepository;
    private final SimulationRepository simulationRepository;

    @PostConstruct
    @Transactional
    public void initSimulation() {
        long count = simulationRepository.count();

        if (count == 0) {
            Simulation sim = new Simulation();
            sim.setStatus("RUNNING");
            sim.setProgressRate(0.0f);
            sim.setTemp(0.0f);
            sim.setPh(0.0f);
            sim.setDoValue(0.0f);
            sim.setTimeStamp(java.time.LocalDateTime.now());

            simulationRepository.save(sim);

            System.out.println("초기 시뮬레이션 1건 생성 완료");
        } else {
            System.out.println("이미 simulation 데이터 존재: " + count + "건");
        }
    }

    //데이터 시뮬레이터
    @Scheduled(fixedRate = 5 * 1000) //10초
    @Transactional
    public void runIntegratedSimulation() {
        List<Simulation> activeProcesses = simulationRepository.findAllByStatus("RUNNING");

        if (activeProcesses.isEmpty()) return;

        for (Simulation simulation : activeProcesses) {
            //센서 데이터 생성
            float newTemp = (float) (30.0 + Math.random() * 10); //30~40 도
            float newPh = (float) (4.5 + Math.random() * 3);     //4.5 ~ 7.5
            float newDo = (float) (5.0 + Math.random() * 55.0);  //5% ~ 60%

            //진행률 증가 로직
            float currentRate = (simulation.getProgressRate() == null) ? 0.0f : simulation.getProgressRate();
            float nextRate = (currentRate >= 100.0f) ? 0.0f : currentRate + (float)(1.0 + Math.random() * 2.0);
            float finalRate = Math.min(100.0f, nextRate);

            //엔티티 업데이트
            simulation.setTemp(newTemp);
            simulation.setPh(newPh);
            simulation.setDoValue(newDo);
            simulation.setProgressRate(finalRate);
            simulation.setTimeStamp(java.time.LocalDateTime.now());

            //로그 저장 (새로고침 시 데이터 유지용)
            SimulationLog log = new SimulationLog();
            log.setSimulation(simulation);
            log.setTempPh(newTemp);
            log.setPhValue(newPh);
            log.setDoValue(newDo);
            log.setProgressRate(finalRate);
            log.setTimeStamp(java.time.LocalDateTime.now());
            simulationLogRepository.save(log);

            //웹소켓 전송
            messagingTemplate.convertAndSend("/topic/temperature/" + simulation.getId(), newTemp);
            messagingTemplate.convertAndSend("/topic/ph/" + simulation.getId(), newPh);
            messagingTemplate.convertAndSend("/topic/do/" + simulation.getId(), newDo);
            messagingTemplate.convertAndSend("/topic/progress/" + simulation.getId(), (int)finalRate);
        }

        System.out.println("통합 시뮬레이션 완료: " + activeProcesses.size() + "건");
    }
}
