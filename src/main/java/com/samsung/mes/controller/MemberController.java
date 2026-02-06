package com.samsung.mes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samsung.mes.dto.LoginRequestDTO;
import com.samsung.mes.dto.LoginResponse;
import com.samsung.mes.dto.MemberRequestDTO;
import com.samsung.mes.entity.Member;
import com.samsung.mes.service.MemberService;
import com.samsung.mes.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/*
 현재 컨트롤러에서 하는 일
 /members로 들어오는 요청을 받아서
 회원목록 조회, 회원 1명 조회, 회원가입, 회원삭제, 로그인(JWT토큰 발급)
 */

@RestController
@RequestMapping("/members")
@CrossOrigin(origins = "http://localhost:5173") // 리액트 개발 서버 주소
@RequiredArgsConstructor
public class MemberController {

	//필드 의존성 주입 컨트롤러는 혼자서는 db저장 / 로그인 검증 / 토큰 생성을 안함
    private final MemberService memberService; //회원관련 로직
    private final JwtUtil jwtUtil; //토큰제작
    
    //모든회원조회 : 서비스에서 회원목록을 가져오고 200 ok회원 리스트를 응답으로 보냄
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    //특정회원조회
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Member member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<?> registerMember(@RequestBody MemberRequestDTO dto) {
        // Service에서 저장된 Member 반환
        Member savedMember = memberService.register(dto);

        // 클라이언트에게 저장된 회원 ID와 메시지 전달
        return ResponseEntity.ok(
                String.format("회원가입 완료! ID: %d", savedMember.getId())
        );
    }
    
    
    //회원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("회원 삭제 완료");
    }
    
    //로그인
    //이메일로 회원을 찾고, 비밀번호가 맞는지 확인, 맞으면 멤버객체를 반환하고 틀리면 null값 반환
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO req) {
        //System.out.println("login 진입: " + req.getEmail());

        try {
            Member member = memberService.login(req.getEmail(), req.getPassword());

            String token = jwtUtil.createToken(member.getEmail()); // 여기서도 터질 수 있음
            
            LoginResponse response = new LoginResponse(
            		"success", token, member.getFirstName(), member.getLastName()
            		);

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            e.printStackTrace(); //이게 핵심
            return ResponseEntity.status(500).body("login error");
        }
    }
    
    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){//logout이라는 메서드
    	HttpSession session = request.getSession(false); //세션이 있으면 가져오고 없으면 만들지 말고 null 반환해라
    	//getSession을 true로 쓰면 true는 새 세션을 만들어버리고 로그아웃인데도 세션이 생기는 이상 현상이 발생할 수 있음
    	if (session != null) { //세션이 존재 할 경우 
    		session.invalidate(); //세션 파기
    	}
    	return ResponseEntity.ok().build(); //http 상태코드 200 ok 응답을 보냄
    }
}
