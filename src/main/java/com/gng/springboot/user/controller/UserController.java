package com.gng.springboot.user.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.model.ResponseDto;
import com.gng.springboot.user.model.UserLoginDto;
import com.gng.springboot.user.model.UserRegisterDto;
import com.gng.springboot.user.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * User controller
 * 
 * - /register
 * - /login
 * </pre>
 * @author gchyoo
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
	private final UserService userService;
	
	/**
	 * Register user
	 * @param UserRegisterDto
	 * @return
	 */
	@ApiOperation(value = "사용자 등록")
	@PostMapping("/register")
	public ResponseDto<String> registerUser(
			@Valid @RequestBody(required = true) UserRegisterDto UserRegisterDto
			) {
		log.info("Register user [{}]", UserRegisterDto);
		
		return new ResponseDto<>(ResponseCode.USER_REGISTER_SUCCESS, userService.registerUser(UserRegisterDto));
	}

	/**
	 * Login user
	 * @param userLoginDto
	 * @return
	 */
	@ApiOperation(value = "사용자 로그인")
	@PostMapping("/login")
	public ResponseDto<UserLoginDto> loginUser(
			@Valid @RequestBody(required = true) UserLoginDto userLoginDto
			) {
		log.info("Login user [{}]", userLoginDto);
		
		return new ResponseDto<>(ResponseCode.LOGIN_SUCCESS, userService.loginUser(userLoginDto));
	}
}
