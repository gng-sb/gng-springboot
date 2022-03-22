package com.gng.springboot.jwt.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class GngUserDto {
	private String userId;
	
	private String userPwd;
	
	private String userName;
}
