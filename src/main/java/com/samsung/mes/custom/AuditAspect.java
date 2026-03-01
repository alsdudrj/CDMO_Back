package com.samsung.mes.custom;

import com.samsung.mes.dto.RecipeDTO;
import com.samsung.mes.entity.AuditAction;
import com.samsung.mes.entity.AuditLog;
import com.samsung.mes.entity.Recipe;
import com.samsung.mes.entity.SalesOrder;
import com.samsung.mes.repository.AuditLogRepository;
import com.samsung.mes.repository.RecipeRepository;
import com.samsung.mes.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    //Repository
    private final RecipeRepository recipeRepository;


    @Around("@annotation(auditable)")   //auditable붙은 모든 메서드에 대한 Proxy
    public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        //특정 행동 실행전 기존 데이터를 가로채고 저장 후 특정 행동 실행
        //실행 후 데이터를 저장 하고 audit_log에 저장 후 결과를 리턴
        System.out.println("Current Auth: " + SecurityContextHolder.getContext().getAuthentication());

        Object result;
        String beforeJson = null;
        String afterJson = null;
        Long entityId = null;

        Object[] args = joinPoint.getArgs();

        // ID 추출
        if (args.length > 0 && args[0] instanceof Long) {
            entityId = (Long) args[0];
        }

        // ===== BEFORE 저장 =====
        if (entityId != null &&
                (auditable.action() == AuditAction.UPDATE
                        || auditable.action() == AuditAction.DELETE
                        || auditable.action() == AuditAction.PROCESS)) {

            Object oldEntity = findEntityById(auditable.entity(), entityId);

            beforeJson = convertToAuditJson(oldEntity, auditable.entity());
        }

        // 실제 메서드 실행
        result = joinPoint.proceed();

        // ===== AFTER 저장 =====
        if (entityId != null &&
                (auditable.action() == AuditAction.UPDATE
                        || auditable.action() == AuditAction.PROCESS)) {

            Object newEntity = findEntityById(auditable.entity(), entityId);
            afterJson = convertToAuditJson(newEntity, auditable.entity());
        }
        if (auditable.action() == AuditAction.CREATE) {
            afterJson = convertToAuditJson(result, auditable.entity());

            if (result instanceof RecipeDTO dto) {
                entityId = dto.getId();
            }

            /* else if 여기 위에 if (result instanceof RecipeDTO dto)에 붙여서 추가 (예시로 SalesOrder 적어둠)
            else if (result instanceof SalesOrderDTO dto) {
                entityId = dto.getId();
            }
            */
        }

        AuditLog log = AuditLog.builder()
                .entityName(auditable.entity())
                .entityId(entityId)
                .action(auditable.action().name())
                .username(getCurrentUsername())
                .beforeValue(beforeJson)
                .afterValue(afterJson)
                .createdAt(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);

        return result;
    }


    //유틸 메서드
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "SYSTEM";
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername(); // CustomUserDetails의 getEmail()이 호출됨
        }

        return auth.getName();
    }

    private Object findEntityById(String entityName, Long id) {

        switch (entityName) {

            case "RECIPE":
                return recipeRepository.findById(id).orElse(null);

            /* 추가 예시
            case "엔티티이름":
                return 리포지토리.findById(id).orElse(null);
            */

            default:
                return null;
        }
    }

    private String convertToAuditJson(Object entity, String entityName) {
        if (entity == null) return null;

        if (entity instanceof ResponseEntity<?> response) {
            entity = response.getBody();
        }

        try {
            switch (entityName) {
                case "RECIPE" -> {
                    //RecipeDTO인 경우
                    if (entity instanceof RecipeDTO dto) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", dto.getId());
                        map.put("name", dto.getName());
                        map.put("status", dto.getStatus());
                        //공정 리스트 추가
                        map.put("processes", dto.getProcesses());
                        return objectMapper.writeValueAsString(map);
                    }

                    //Recipe 엔티티인 경우
                    if (entity instanceof Recipe recipe) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", recipe.getId());
                        map.put("name", recipe.getName());
                        map.put("status", recipe.getStatus());

                        //Process를 Map List 형태로 저장
                        if (recipe.getProcesses() != null) {
                            List<Map<String, Object>> processList = recipe.getProcesses().stream()
                                    .map(p -> {
                                        Map<String, Object> pMap = new HashMap<>();
                                        pMap.put("id", p.getId());
                                        pMap.put("name", p.getName());
                                        pMap.put("stepOrder", p.getStepOrder());
                                        pMap.put("temp", p.getTemp());
                                        return pMap;
                                    }).collect(Collectors.toList());
                            map.put("processes", processList);
                        }
                        return objectMapper.writeValueAsString(map);
                    }

                    /* 추가 예시 (사용할 Entity랑 DTO 이름으로 변경 하고 포함 시킬 log 값을 put으로 넣음) sales_order를 예시로 넣음
                    case "SALES_ORDER" -> {
                        if (entity instanceof SalesOrderDTO dto) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", dto.getId());
                            map.put("orderNo", dto.getOrderNo());           // 주문번호
                            map.put("customerName", dto.getCustomerName()); // 고객사
                            map.put("totalAmount", dto.getTotalAmount());   // 총액
                            return objectMapper.writeValueAsString(map);
                        }

                        if (entity instanceof SalesOrder order) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", order.getId());
                            map.put("orderNo", order.getOrderNo());
                            map.put("status", order.getStatus());
                            return objectMapper.writeValueAsString(map);
                        }
                    }
                    */

                    return objectMapper.writeValueAsString(Map.of("id", getIdSafely(entity)));
                }
                default -> {
                    return objectMapper.writeValueAsString(Map.of("id", getIdSafely(entity)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"audit serialize failed\"}";
        }
    }

    private Long getIdSafely(Object entity) {
        try {
            Method getId = entity.getClass().getMethod("getId");
            return (Long) getId.invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }
}
