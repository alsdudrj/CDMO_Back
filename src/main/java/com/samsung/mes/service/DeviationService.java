package com.samsung.mes.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import com.samsung.mes.custom.Auditable;
import com.samsung.mes.entity.AuditAction;
import com.samsung.mes.entity.Deviation;
import com.samsung.mes.repository.DeviationRepository;
import com.samsung.mes.spec.DeviationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.samsung.mes.dto.DeviationDTO;
import com.samsung.mes.dto.SignatureRequest;
import org.springframework.transaction.annotation.Transactional;
import com.samsung.mes.dto.DeviationApprovalDTO;

@Service
@RequiredArgsConstructor
public class DeviationService {

    //(26.03.02 민영추가)
    private final DeviationRepository deviationRepository;
    private final SignatureService signatureService; // 의존성 주입 추가

    @Transactional
    public void approveDeviationWithSignature(Long deviationId, DeviationApprovalDTO request) {
        // 1. 일탈 정보 조회
        Deviation deviation = deviationRepository.findById(deviationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일탈을 찾을 수 없습니다."));

        // 2. 전자서명 검증 및 생성 (SignatureService 위임)
        signatureService.createSignatureForDeviation(deviation, request);

        // 3. 일탈 상태 업데이트
        // 기존 엔티티의 상태 변경 규칙에 따라 'CLOSED' 상태로 업데이트합니다. [cite: 5, 6]
        deviation.setStatus("CLOSED");
        deviation.setIsClosed(true);

        deviationRepository.save(deviation);
    }



    private final Random random = new Random();

    @Auditable(action = AuditAction.CREATE, entity = "DEVIATION")
    public DeviationDTO simulateDeviation() {
        String[] parameters = {"Temperature", "pH", "Dissolved Oxygen"};
        String parameter = parameters[random.nextInt(parameters.length)];

        // Determine severity based on weighted random
        int rand = random.nextInt(100);
        String severity;
        if (rand < 75) {
            severity = "MINOR";
        } else if (rand < 95) { // 75 + 20
            severity = "MAJOR";
        } else { // Remaining 5
            severity = "CRITICAL";
        }

        double limitValue = 0.0;
        double recordedValue = 0.0;

        // Define base values and deviation logic per parameter
        if ("Temperature".equals(parameter)) {
            limitValue = 37.0; // Example limit for cell culture
            // Deviation logic: higher severity means larger deviation
            double deviation = getDeviationAmount(severity, 0.5, 2.0, 5.0);
            recordedValue = limitValue + deviation * (random.nextBoolean() ? 1 : -1);
        } else if ("pH".equals(parameter)) {
            limitValue = 7.2;
            double deviation = getDeviationAmount(severity, 0.1, 0.5, 1.0);
            recordedValue = limitValue + deviation * (random.nextBoolean() ? 1 : -1);
        } else if ("Dissolved Oxygen".equals(parameter)) {
            limitValue = 50.0; // % saturation
            double deviation = getDeviationAmount(severity, 5.0, 15.0, 30.0);
            recordedValue = limitValue + deviation * (random.nextBoolean() ? 1 : -1);
        }

        // Round recordedValue to 2 decimal places
        recordedValue = Math.round(recordedValue * 100.0) / 100.0;

        //(26.03.02 민영추가) 일탈 데이터 id build를 위해 Entitiy생성
        Deviation deviation = new Deviation();
        deviation.setParameter(parameter);
        deviation.setRecordedValue(recordedValue);
        deviation.setLimitValue(limitValue);
        deviation.setSeverity(severity);
        deviation.setStatus("OPEN");
        deviation.setIsClosed(false);
        deviation.setCreatedAt(LocalDateTime.now());

        Deviation saved = deviationRepository.save(deviation);

        return DeviationDTO.builder()
                .id(saved.getId())
                .batchId(UUID.randomUUID().toString())
                .parameter(parameter)
                .recordedValue(recordedValue)
                .limitValue(limitValue)
                .severity(severity)
                .status(saved.getStatus())
                .isClosed(saved.getIsClosed())
                .build();
    }

    private double getDeviationAmount(String severity, double minorBase, double majorBase, double criticalBase) {
        double noise = random.nextDouble() * minorBase; // add some noise
        switch (severity) {
            case "MINOR":
                return minorBase + noise;
            case "MAJOR":
                return majorBase + noise;
            case "CRITICAL":
                return criticalBase + noise;
            default:
                return minorBase;


        }
    }

    //(26.03.16 민영추가) 검색기능 추가
    @Transactional(readOnly = true)
    public Page<DeviationDTO> searchDeviations(
            String severity,
            String status,
            String keyword,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ){

        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end = endDate != null ? endDate.atTime(23,59,59) : null;

        Specification<Deviation> spec = Specification
                .where(DeviationSpecification.severityEquals(severity))
                .and(DeviationSpecification.statusEquals(status))
                .and(DeviationSpecification.keywordLike(keyword))
                .and(DeviationSpecification.createdAfter(start))
                .and(DeviationSpecification.createdBefore(end));

        Page<Deviation> deviations = deviationRepository.findAll(spec, pageable);

        return deviations.map(this::convertToDTO);
    }

    private DeviationDTO convertToDTO(Deviation deviation){
        return DeviationDTO.builder()
                .id(deviation.getId())
                .batchId(deviation.getBatch() != null ? deviation.getBatch().getBatchNo() : "N/A")
                .parameter(deviation.getParameter())
                .recordedValue(deviation.getRecordedValue())
                .limitValue(deviation.getLimitValue())
                .severity(deviation.getSeverity())
                .status(deviation.getStatus())
                .isClosed(deviation.getIsClosed())
                .createdAt(deviation.getCreatedAt())
                .build();

    }
}
