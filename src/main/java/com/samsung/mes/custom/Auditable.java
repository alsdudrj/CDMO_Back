package com.samsung.mes.custom;

import com.samsung.mes.entity.AuditAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)         //메서드에만 사용가능
@Retention(RetentionPolicy.RUNTIME) //실행 중에도 유지가능 (AOP가 읽기 가능)
public @interface Auditable {
    AuditAction action();       // CREATE, UPDATE, DELETE
    String entity();
}
