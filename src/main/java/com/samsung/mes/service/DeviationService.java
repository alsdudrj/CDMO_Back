package com.samsung.mes.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.samsung.mes.custom.Auditable;
import com.samsung.mes.entity.AuditAction;
import com.samsung.mes.entity.Deviation;
import com.samsung.mes.entity.Recipe;
import com.samsung.mes.entity.Process;

import com.samsung.mes.repository.DeviationRepository;
import com.samsung.mes.spec.DeviationSpecification;
import com.samsung.mes.repository.RecipeRepository; // ✨ 추가
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

    private final DeviationRepository deviationRepository;
    private final SignatureService signatureService; // 의존성 주입 추가
    private final RecipeRepository recipeRepository; // ✨ 의존성 주입 추가

    private final Random random = new Random();

    @Transactional
    public void approveDeviationWithSignature(Long deviationId, DeviationApprovalDTO request) {
        // 1. 일탈 정보 조회
        Deviation deviation = deviationRepository.findById(deviationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일탈을 찾을 수 없습니다."));

        // 2. 전자서명 검증 및 생성 (SignatureService 위임)
        signatureService.createSignatureForDeviation(deviation, request);

        // 3. 일탈 상태 업데이트
        deviation.setStatus("CLOSED");
        deviation.setIsClosed(true);

        deviationRepository.save(deviation);
    }

    @Auditable(action = AuditAction.CREATE, entity = "DEVIATION")
    public DeviationDTO simulateDeviation() {

        // ✨ 1. DB에서 활성화된(Active) 레시피를 가져옵니다.
        List<Recipe> activeRecipes = recipeRepository.findByIsActiveTrue();
        if (activeRecipes.isEmpty()) {
            throw new IllegalStateException("활성화된 레시피가 없습니다.");
        }
        // 시뮬레이션을 위해 활성화된 레시피 중 하나를 랜덤으로 선택
        Recipe recipe = activeRecipes.get(random.nextInt(activeRecipes.size()));

        // ✨ 2. 해당 레시피의 첫 번째 Process(세부 공정) 파라미터 기준값을 가져옵니다.
        if (recipe.getProcesses().isEmpty()) {
            throw new IllegalStateException("해당 레시피에 공정(Process) 데이터가 없습니다.");
        }
        Process process = recipe.getProcesses().get(0);

        String[] parameters = {"Temperature", "pH", "Dissolved Oxygen"};
        String parameter = parameters[random.nextInt(parameters.length)];

        int rand = random.nextInt(100); // [cite: 391]
        String severity = (rand < 75) ? "MINOR" : (rand < 95) ? "MAJOR" : "CRITICAL";

        double limitValue = 0.0;
        double recordedValue = 0.0;

        // ✨ 3. 하드코딩 제거! Process 엔티티의 실제 설정값을 가져옵니다.
        // (주의: Process 엔티티의 Getter 메서드 이름은 실제 구현에 맞게 수정해주세요)
        if ("Temperature".equals(parameter)) {
            limitValue = process.getTemp();
            double deviation = getDeviationAmount(severity, 0.5, 2.0, 5.0); // [cite: 397]
            recordedValue = limitValue + deviation * (random.nextBoolean() ? 1 : -1); // [cite: 398]
        } else if ("pH".equals(parameter)) {
            limitValue = process.getPh();
            double deviation = getDeviationAmount(severity, 0.1, 0.5, 1.0); // [cite: 400]
            recordedValue = limitValue + deviation * (random.nextBoolean() ? 1 : -1);
        } else if ("Dissolved Oxygen".equals(parameter)) {
            limitValue = process.getDoValue(); // DO 필드 Getter 매핑
            double deviation = getDeviationAmount(severity, 5.0, 15.0, 30.0); // [cite: 402]
            recordedValue = limitValue + deviation * (random.nextBoolean() ? 1 : -1); // [cite: 403]
        }

        recordedValue = Math.round(recordedValue * 100.0) / 100.0; // [cite: 404]


        // ✨ 4. 엔티티에 실제 레시피 정보와 수치를 꼼꼼히 저장합니다.
        Deviation deviation = new Deviation(); // [cite: 405]
        deviation.setRecipe(recipe);
        deviation.setParameter(parameter);
        deviation.setLimitValue(limitValue);
        deviation.setRecordedValue(recordedValue);
        deviation.setSeverity(severity); // [cite: 406]
        deviation.setStatus("OPEN");
        deviation.setIsClosed(false);
        deviation.setCreatedAt(LocalDateTime.now());

        Deviation saved = deviationRepository.save(deviation);

        return DeviationDTO.builder()
                .id(saved.getId())
                // TODO: Batch 연동 시 실제 batchId로 교체 필요
                .batchId(UUID.randomUUID().toString())
                .parameter(parameter)
                .recordedValue(recordedValue)
                .limitValue(limitValue)
                .severity(severity) // [cite: 407]
                .status(saved.getStatus())
                .isClosed(saved.getIsClosed())
                .build(); // [cite: 408]
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
