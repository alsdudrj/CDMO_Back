package com.samsung.mes.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration //스프링 설정 클래스 (빈 등록/보안 설정)
@EnableWebSecurity //Spring Security기능을 활성화 보안설정 기준으로 사용하게 해줌
public class SecurityConfig{ //보안설정을 쉽게 overrid(재정의)해서 쓰는 기본 틀
	
	@Bean //db에 비밀번호를 저장할 때 그대로 저장하면 보안 문제가 생겨서 인코딩 시켜서 저장
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); //BCrypt는 비밀번호 해싱에 사용되는 방식 중 하나
		//같은 비밀번호여도 매번 결과가 다르게 나오도록 (salt)설계되어 있음
	}
	
	//CORS 설정
	@Bean
	public CorsConfigurationSource corsConfigurationSource () {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);											//쿠키 / 세션 / 인증정보(Authorization 헤더 포함 등)를 브라우저 요청에 포함하는 걸 허용
		config.setAllowedOriginPatterns(List.of("http://localhost:5173"));			//허용할 주소 설정
		config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));	//허용할 http 메서드 목록 설정
		config.setAllowedHeaders(List.of("*"));										//프론트에서 보내는 헤더는 모두 허용 EX) Content-Type, Authorization 등
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); 
		source.registerCorsConfiguration("/**", config);							//모든 경로의 CORS 허용
		
		return source;
	}
	
	//실제 보안규칙 설정 (누가 어떤 API접근이 가능한지)
	//@Override Class에 확장이 사라져서 재정의 할것이 사라짐
	@Bean //그래서 스프링이 관리하는 공식설정으로 등록 해줌
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		//SecurityFilterChain => 검사하는 규칙들의 줄, 보안 검사 규칙세트 | 어떤 요청은 통과하고 어떤 요청은 막을지 설정
		
		http
		.cors(cors -> cors.configurationSource(corsConfigurationSource()))//허용하는 규칙연결
		.csrf(csrf -> csrf.disable())		//브라우저가 쿠키를 자동 첨부하는 것 기능을 끔
		.formLogin(form -> form.disable())	//기본 로그인 페이지를 자동으로 만들게 되어있는데 따로 프론트앤드로 ui만들기 때문에 끔
		.httpBasic(basic -> basic.disable())//로그인 팝업뜨는 방식, off
		.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.POST, "/members/login").permitAll()		//로그인 안해도 통과 가능
				.requestMatchers("/members/login", "/members/register","/members/logout").permitAll()
				.requestMatchers("/api/**").permitAll()
				.requestMatchers("/api/sales/orders/**").permitAll()
				.requestMatchers("/", "/error", "/favicon.ico").permitAll()
                .requestMatchers("/ws-cdms/**").permitAll()
				.anyRequest().authenticated()//위에서 허용된 것을 제외하고는 모두 로그인(인증)된 사용자만 접근가능
		);
		
		return http.build(); //완성된 규칙을 최종적으로 서버에 전송하여 적용
	}
}
