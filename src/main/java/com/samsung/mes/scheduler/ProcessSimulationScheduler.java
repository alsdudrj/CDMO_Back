package com.samsung.mes.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessSimulationScheduler {

    private final SimpMessagingTemplate messagingTemplate;

    // 5초마다 실행 (실시간 시뮬레이션)
    @Scheduled(fixedRate = 5000)
    public void simulateProcess() {
        int randomProgress = (int) (Math.random() * 100);

        // 실시간 데이터를 구독 중인 모든 클라이언트에게 전송 (Topic 방식)
        // 경로는 /topic/progress/{공정ID} 형태가 될 수 있습니다.
        messagingTemplate.convertAndSend("/topic/progress/1", randomProgress);

        System.out.println("실시간 데이터 전송 중: " + randomProgress + "%");
    }
}
