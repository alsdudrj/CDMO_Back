package com.samsung.mes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users") // 실제 DB 테이블 이름
@Getter
@Setter
public class User {

    @Id
    private String id; // UserRepository에서 String 타입으로 지정했으므로 동일하게 맞춰줍니다.

    private String password;

    private String name;

    private String role; // 예: "ADMIN", "USER" 등 권한 관리용

    // 서비스 코드에서 에러가 났던 비밀번호 검증 메서드
    public boolean checkPassword(String inputPassword) {
        // 실제 운영 환경에서는 평문 비교가 아닌 BCrypt 등의 암호화 비교 로직이 들어가야 합니다.
        return this.password.equals(inputPassword);
    }
}