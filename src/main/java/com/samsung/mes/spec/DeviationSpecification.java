package com.samsung.mes.spec;

import com.samsung.mes.entity.Deviation;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class DeviationSpecification {

    public static Specification<Deviation> severityEquals(String severity){
        return (root, query, cb) ->
                severity == null ? null : cb.equal(root.get("severity"), severity);
    }

    public static Specification<Deviation> statusEquals(String status){
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Deviation> keywordLike(String keyword){
        return (root, query, cb) -> {

            if(keyword == null) return null;

            var parameterLike = cb.like(
                    cb.lower(root.get("parameter")),
                    "%" + keyword.toLowerCase() + "%"
            );

            var batchJoin = root.join("batch", JoinType.LEFT);

            var batchLike = cb.like(
                    cb.lower(batchJoin.get("batchNo")),
                    "%" + keyword.toLowerCase() + "%"
            );

            return cb.or(parameterLike, batchLike);
        };
    }

    public static Specification<Deviation> createdAfter(LocalDateTime start){
        return (root, query, cb) ->
                start == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), start);
    }

    public static Specification<Deviation> createdBefore(LocalDateTime end){
        return (root, query, cb) ->
                end == null ? null : cb.lessThanOrEqualTo(root.get("createdAt"), end);
    }

}
