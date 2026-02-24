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
import java.util.List;

@Component
@RequiredArgsConstructor
public class SimulationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ProcessLogRepository processLogRepository;
    private final ProcessRepository processRepository;

    // 1. 센서 데이터 시뮬레이터 (온도, pH, DO만 담당)
    @Scheduled(fixedRate = 5 * 1000) // 5초 혹은 10초 하나로 통합
    @Transactional
    public void runIntegratedSimulation() {
        List<Process> activeProcesses = processRepository.findAllByStatus("RUNNING");

        if (activeProcesses.isEmpty()) return;

        for (Process process : activeProcesses) {
            // 1. 센서 데이터 생성
            float newTemp = (float) (30.0 + Math.random() * 10);
            float newPh = (float) (4.5 + Math.random() * 3);
            float newDo = (float) (5.0 + Math.random() * 55.0);

            // 2. 진행률 증가 로직 (기존 0~100 증가 로직 통합)
            float currentRate = (process.getProgressRate() == null) ? 0.0f : process.getProgressRate();
            float nextRate = (currentRate >= 100.0f) ? 0.0f : currentRate + (float)(1.0 + Math.random() * 2.0);
            float finalRate = Math.min(100.0f, nextRate);

            // 3. 엔티티 업데이트
            process.setTemp(newTemp);
            process.setPh(newPh);
            process.setDoValue(newDo);
            process.setProgressRate(finalRate);
            process.setTimeStamp(java.time.LocalDateTime.now());

            // 4. 로그 저장 (새로고침 시 데이터 유지용)
            ProcessLog log = new ProcessLog();
            log.setProcess(process);
            log.setTempPh(newTemp);
            log.setPhValue(newPh);
            log.setDoValue(newDo);
            log.setProgressRate(finalRate);
            log.setTimeStamp(java.time.LocalDateTime.now());
            processLogRepository.save(log);

            // 5. 웹소켓 전송
            messagingTemplate.convertAndSend("/topic/temperature/" + process.getId(), newTemp);
            messagingTemplate.convertAndSend("/topic/ph/" + process.getId(), newPh);
            messagingTemplate.convertAndSend("/topic/do/" + process.getId(), newDo);
            messagingTemplate.convertAndSend("/topic/progress/" + process.getId(), (int)finalRate);
        }

        // saveAll은 루프 밖에서 한 번만 실행해도 @Transactional 덕분에 자동 반영됩니다.
        System.out.println("통합 시뮬레이션 완료: " + activeProcesses.size() + "건");
    }
}

//    // 2. 공정 진행률 시뮬레이터 (0 -> 100 증가 로직)
//    @Scheduled(fixedRate = 5 * 1000) // 5초마다 실행
//    @Transactional
//    public void runSimulation() {
//        // ID가 1L인 프로세스를 기준으로 시뮬레이션
//        Process process = processRepository.findById(1L).orElseGet(() -> {
//            Process newProcess = new Process();
//            newProcess.setName("기본 공정");
//            newProcess.setStatus("RUNNING");
//            newProcess.setTempPh(0.0f);
//            newProcess.setPhValue(7.0f);
//            newProcess.setDoValue(80.0f);
//            newProcess.setProgressRate(0.0f); // 초기값 0
//            return processRepository.save(newProcess);
//        });
//
//        // 현재 진행률 가져오기 (null 체크)
//        float currentRate = (process.getProgressRate() == null) ? 0.0f : process.getProgressRate();
//
//        // 0~100까지 증가하는 로직
//        float nextRate;
//        if (currentRate >= 100.0f) {
//            nextRate = 0.0f; // 100% 완료 시 다시 0%부터 시작 (테스트용)
//        } else {
//            // 5초마다 1% ~ 3% 사이로 랜덤하게 증가 (순차적 증가)
//            nextRate = currentRate + (float)(1.0 + Math.random() * 2.0);
//        }
//
//        // 100을 넘지 않도록 제한
//        float finalRate = Math.min(100.0f, nextRate);
//
//        // DB 업데이트
//        process.setProgressRate(finalRate);
//        process.setStatus("RUNNING");
//        process.setTimeStamp(java.time.LocalDate.now());
//        processRepository.save(process);
//
//        // 웹소켓 전송 (정수형으로 전송)
//        messagingTemplate.convertAndSend("/topic/progress/" + process.getId(), (int)finalRate);
//
//        System.out.println("공정 진행률 업데이트: ID(" + process.getId() + ") -> " + (int)finalRate + "%");
//    }
//}
//
//    //온도 시뮬레이터
//    @Scheduled(fixedRate = 10 * 1000)
//    @Transactional
//    public void runTemperatureSimulation() {
//        // ID 1번 공정을 대상으로 온도 데이터 생성
//        Process process = processRepository.findById(1L).orElse(null);
//
//        if (process != null) {
//            // 30~40도 사이의 랜덤 온도 생성
//            float randomTemp = (float) (30.0 + Math.random() * 10);
//
//            // DB에 저장
//            process.setTempPh(randomTemp);
//            processRepository.save(process);
//
//            // 실시간 송신
//            messagingTemplate.convertAndSend("/topic/temperature/" + process.getId(), randomTemp);
//
//            System.out.println("실시간 온도데이터 전송 중: " + String.format("%.1f", randomTemp) + "°C");
//        }
//    }
//
//    //pH, DO 수치 시뮬레이터
//    @Scheduled(fixedRate = 10 * 1000)
//    @Transactional
//    public void runChemicalSimulation() {
//        Process process = processRepository.findById(1L).orElse(null);
//
//        if (process != null) {
//            // 데이터 생성
//            float ph = (float) (4.5 + Math.random() * 3); // 4.5 ~ 7.5 사이
//            float doVal = (float) (5.0 + Math.random() * 55.0); // 5.0 ~ 60.0 사이
//
//            // DB 저장
//            process.setPhValue(ph);
//            process.setDoValue(doVal);
//            processRepository.save(process);
//
//            // 실시간 전송
//            messagingTemplate.convertAndSend("/topic/ph/" + process.getId(), ph);
//            messagingTemplate.convertAndSend("/topic/do/" + process.getId(), doVal);
//
//            System.out.println(String.format("pH: %.2f | DO: %.1f%%", ph, doVal));
//        }
//    }
