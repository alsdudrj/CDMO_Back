package com.samsung.mes.service;

import com.samsung.mes.entity.Process;
import com.samsung.mes.entity.ProcessLog;
import com.samsung.mes.repository.ProcessLogRepository;
import com.samsung.mes.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SimulationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ProcessLogRepository logRepository;
    private final ProcessRepository processRepository;

    //공정진행률 시뮬레이터
    @Scheduled(fixedRate = 5 * 1000)
    @Transactional
    public void runSimulation() {
        Process process = processRepository.findById(1L).orElseGet(() -> {
            Process newProcess = new Process();
            newProcess.setName("기본 공정");
            newProcess.setStatus("RUNNING");
            newProcess.setDescription("시뮬레이션 자동 생성 데이터");
            newProcess.setTempPh(0.0f);         // 초기값 설정
            newProcess.setProgressRate(0.0f);   // 초기값 설정
            return processRepository.save(newProcess);
        });

        float randomRate = (float) (Math.random() * 100);   //진행률 생성

        // 진행률 전용 필드(progressRate)에 저장
        process.setProgressRate(randomRate);
        process.setStatus("RUNNING");
        process.setTimeStamp(java.time.LocalDate.now());

        processRepository.save(process);

        messagingTemplate.convertAndSend("/topic/progress/" + process.getId(), (int)randomRate);
        System.out.println("실시간 데이터 전송 및 DB 저장 완료: " + (int)randomRate + "%");
    }

    //온도 시뮬레이터
    @Scheduled(fixedRate = 10 * 1000)
    @Transactional
    public void runTemperatureSimulation() {
        // ID 1번 공정을 대상으로 온도 데이터 생성
        Process process = processRepository.findById(1L).orElse(null);

        if (process != null) {
            // 30~40도 사이의 랜덤 온도 생성
            float randomTemp = (float) (30.0 + Math.random() * 10);

            // DB에 저장
            process.setTempPh(randomTemp);
            processRepository.save(process);

            // 실시간 송신
            messagingTemplate.convertAndSend("/topic/temperature/" + process.getId(), randomTemp);

            System.out.println("실시간 온도데이터 전송 중: " + String.format("%.1f", randomTemp) + "°C");
        }
    }

    //pH, DO 수치 시뮬레이터
    @Scheduled(fixedRate = 10 * 1000)
    @Transactional
    public void runChemicalSimulation() {
        Process process = processRepository.findById(1L).orElse(null);

        if (process != null) {
            // 데이터 생성
            float ph = (float) (4.5 + Math.random() * 3); // 4.5 ~ 7.5 사이
            float doVal = (float) (5.0 + Math.random() * 55.0); // 5.0 ~ 60.0 사이

            // DB 저장
            process.setPhValue(ph);
            process.setDoValue(doVal);
            processRepository.save(process);

            // 실시간 전송
            messagingTemplate.convertAndSend("/topic/ph/" + process.getId(), ph);
            messagingTemplate.convertAndSend("/topic/do/" + process.getId(), doVal);

            System.out.println(String.format("pH: %.2f | DO: %.1f%%", ph, doVal));
        }
    }
}