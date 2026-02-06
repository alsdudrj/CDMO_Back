package com.samsung.mes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.samsung.mes.dto.MemberRequestDTO;
import com.samsung.mes.entity.Member;
import com.samsung.mes.repository.MemberRepository;

import jakarta.transaction.Transactional;

@Service
//@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository; //멤버변수2개 의존성 주입
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Transactional //문제생기면 전체 롤백을 위해 
	public Member register(MemberRequestDTO dto) { //회원가입
		
		if(memberRepository.existsByEmail(dto.getEmail())) { //이메일 중복검사 후 가입을 막음
			throw new IllegalArgumentException("이미 가입된 이메일 입니다.");
		}
		
		String encryptedPw = passwordEncoder.encode(dto.getPassword());
		//암호화된 문자열로 바꿔 저장

		// DTO -> Entity (Builder 이용)
        Member member = Member.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(encryptedPw) // 실서비스에서는 암호화 필수
              //.repeatPassword(dto.getRepeatPassword())
                .gender(dto.getGender())
                .companyName(dto.getCompanyName())
                .position(dto.getPosition())
                .tel(dto.getTel())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .build();

        return memberRepository.save(member); //db에 저장하고 저장된 객체를 리턴
    }
	
	public List<Member> getAllMembers(){ //전체회원 조회
		return memberRepository.findAll();
	}
	
	public Member getMemberById(Long id) { //특정회원 조회
		return memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("" + id));
	}
	
	@Transactional
	public void deleteMember(Long id) { //존재하는지 확인 후 삭제
		if(!memberRepository.existsById(id)) {
			throw new IllegalArgumentException("삭제할 회원이 존재하지 않음" + id);
			
		}
	}
	
	public Member login(String email, String password) {
		Member member = memberRepository.findByEmail(email.trim()).orElseThrow(() -> new RuntimeException("존재하지 않는 이메일"));
		
		boolean ok = passwordEncoder.matches(password, member.getPassword());
		System.out.println("" + ok);
		
		if(!ok) { //비밀번호 틀리면 예외, 맞으면 회원을 반환
			throw new RuntimeException("비밀번호 불일치");
		}
		
		return member;
	}
}



/*
// 1. 비밀번호 일치 검사
 
if (!req.getPassword().equals(req.getRepeatPassword())) {
    throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
}

// 2. 이메일 중복 검사
memberRepository.findByEmail(req.getEmail())
        .ifPresent(m -> {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        });


*/