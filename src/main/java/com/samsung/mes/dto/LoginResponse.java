package com.samsung.mes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
	private String result;
	private String token;
	private String firstName;
	private String lastName;
}
