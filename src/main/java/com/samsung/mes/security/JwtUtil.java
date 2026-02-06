package com.samsung.mes.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	//jwt는 그냥 문자열이 아니라 내용이 위조되지 않았다는 걸 보장하기 위해 서명(signature)를 붙여서 발급
	//그 서명에 붙일 비밀 재료가  secret이고 HS256알고리즘은 보통 32바이트 이상 키가 필요하다
	
	//최소 32바이트 이상
	private static final String SECRET = "this-is-very-long-secret-key-at-least-32-bytes";
	
	//key객체 생성 (secret을 실제 키로 변환)
	private final Key key = Keys.hmacShaKeyFor(
				SECRET.getBytes(StandardCharsets.UTF_8)
			);
	
	public String createToken(String email) {
		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
}
