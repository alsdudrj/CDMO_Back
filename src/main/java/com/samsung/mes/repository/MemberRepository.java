package com.samsung.mes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samsung.mes.entity.Member;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//DB랑 대화하는 전담 인터페이스, sql을 직접 안써도 메서드 이름만으로 select, insert, update, delete를 만들어줌
//결과적으로 Member 테이블을 다루는 DB 관리자
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
	Optional<Member> findByEmail(String email); //이메일로 회원 찾기, 이메일 컬럼을 기준으로 회원 한명을 찾아서 있으면 member / 없으면 null
	//Optional은 해도되고 안해도 됨 > email이 DB에 없을수도 있기 때문에 NullPointerException가 일어나는것을 방지하기 위해 사용
	
	boolean existsByEmail(String email); //이메일 존재 여부 체크 (true면 중복으로 가입 x)
	
	Optional<Member> findByProviderAndProviderId(String provider, String providerId); //소셜 로그인용 조회 (kakao, google 등)
}

/*
 Repository는 DB에 대한 담당자
 Member 엔티티에 대한 DB의 데이터 저장, 삭제, 조회 등을 담당
 class가 아닌 interface인 이유
 1) 구현체를 우리가 직접 안만듬
 2) Spring Data jpa가 실행시점에서 자동으로 구현 클래스를 만들어서 bean으로 등록
 3) 이런 메서드가 필요하다 정도로 정의
 
 JpaRepository<Member, Long> : Entity와 Entity의 pk 식별자 타입
 이걸로 기본 메서드들이 자동으로 생김
 ㄴ save() => insert 또는 update
 ㄴ saveAll() => 여럭개 한꺼번에 저장
 ㄴ findById(id) => pk로 한 행 조회 / 없으면 Optional.empty()
 ㄴ exitsById(id) => 해당 pk가 존재하는지만 boolean 타입으로 확인
 ㄴ findAll() => 전체 조회
 ㄴ findAllById(ids) => 여러 아이디를 한번에 조회
 ㄴ delete(member) => 삭제
 ㄴ deleteById(id) => pk조회로 삭제
 ㄴ deleteAll(entities) 전달된 목록만 삭제 / deleteAll() 처럼 공백이면 테이블 전체 삭제
 ㄴ count() => 갯수 세기
 등등
 */
