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

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void runSimulation() {
        Process process = processRepository.findById(1L).orElseGet(() -> {
            Process newProcess = new Process();
            newProcess.setName("기본 공정");
            newProcess.setStatus("RUNNING");
            newProcess.setDescription("시뮬레이션 자동 생성 데이터");
            newProcess.setTempPh(0.0f); // float 리터럴은 뒤에 f를 붙입니다.
            return processRepository.save(newProcess);
        });

        // Math.random()은 double을 반환하므로 float으로 캐스팅(형변환) 합니다.
        float randomRate = (float) (Math.random() * 100);

        // 3. 필드 설정 (타입이 float이므로 이제 에러가 나지 않습니다)
        process.setTempPh(randomRate);
        process.setStatus("RUNNING");

        // 타임스탬프가 LocalDate이므로 오늘 날짜를 넣어줍니다.
        process.setTimeStamp(java.time.LocalDate.now());

        processRepository.save(process);

        // 4. 프론트 전송 (소수점이 너무 길면 보기 힘드니 int로 변환해서 보냅니다)
        messagingTemplate.convertAndSend("/topic/progress/" + process.getId(), (int)randomRate);
        System.out.println("실시간 데이터 전송 및 DB 저장 완료: " + (int)randomRate + "%");
    }
}