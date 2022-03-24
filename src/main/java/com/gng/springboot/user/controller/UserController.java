package com.gng.springboot.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gng.springboot.commons.exception.model.ResponseCode;
import com.gng.springboot.commons.model.ResponseDto;
import com.gng.springboot.user.model.UserLoginDto;
import com.gng.springboot.user.model.UserRegisterDto;
import com.gng.springboot.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
	private final UserService userService;
	
	@PostMapping("/register")
	public ResponseDto<String> registerUser(
			@RequestBody(required = true) UserRegisterDto UserRegisterDto
			) {
		log.info("Register user [{}]", UserRegisterDto);
		
		return new ResponseDto<>(ResponseCode.USER_REGISTER_SUCCESS, userService.registerUser(UserRegisterDto));
	}
	
	@PostMapping("/login")
	public ResponseDto<UserLoginDto> loginUser(
			@RequestBody(required = true) UserLoginDto userLoginDto
			) {
		log.info("Login user [{}]", userLoginDto);
		
		return new ResponseDto<>(ResponseCode.LOGIN_SUCCESS, userService.loginUser(userLoginDto));
	}
}
