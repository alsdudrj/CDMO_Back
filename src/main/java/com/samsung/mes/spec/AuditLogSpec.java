package com.samsung.mes.spec;

import com.samsung.mes.entity.AuditLog;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditLogSpec {
    public static Specification<AuditLog> search(
            String keyword,
            String action,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isEmpty()) {
                Predicate username = cb.like(root.get("username"), "%" + keyword + "%");
                Predicate entity = cb.like(root.get("entityName"), "%" + keyword + "%");

                predicates.add(cb.or(username, entity));
            }

            if (action != null && !action.isEmpty()) {
                predicates.add(cb.equal(root.get("action"), action));
            }

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
            }

            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
