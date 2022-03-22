package com.gng.springboot.jwt.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gng.springboot.commons.model.ResponseDto;
import com.gng.springboot.jwt.model.GngUserDto;
import com.gng.springboot.jwt.service.JwtUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(path = "/jwt")
@RequiredArgsConstructor
@RestController
public class JwtUserController {
	private final JwtUserService jwtUserService;
	
	@PostMapping("/register")
	public ResponseDto<String> registerUser(
			@RequestBody(required = true) GngUserDto gngUserDto
			) {
		// TODO: 비밀번호 저장 시 암호화 필요
		return jwtUserService.registerUser(gngUserDto);
	}
}
