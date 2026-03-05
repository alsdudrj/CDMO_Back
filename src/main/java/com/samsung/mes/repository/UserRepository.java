package com.samsung.mes.repository;

import com.samsung.mes.entity.User; // 톰캣 User가 아닌, 직접 만든 User 엔티티를 임포트해야 합니다.
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // JpaRepository<User, String> 상속만으로도
    // findById(), save(), delete() 등의 기본 메서드를 모두 사용할 수 있습니다.

    // 필요하다면 아래와 같이 이름이나 사번 등으로 검색하는 커스텀 메서드를 추가할 수 있습니다.
    // Optional<User> findByEmpNo(String empNo);
}