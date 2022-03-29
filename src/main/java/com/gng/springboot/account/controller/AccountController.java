package com.gng.springboot.account.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gng.springboot.account.model.AccountLoginDto;
import com.gng.springboot.account.model.AccountRegisterDto;
import com.gng.springboot.account.service.AccountService;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.model.ResponseDto;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * Account controller
 * 
 * - /account/register
 * - /account/login
 * </pre>
 * @author gchyoo
 *
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/account")
@RestController
public class AccountController {
	private final AccountService accountService;
	
	/**
	 * Register account
	 * @param AccountRegisterDto
	 * @return
	 */
	@ApiOperation(value = "사용자 등록")
	@PostMapping("/register")
	public ResponseDto<String> registerAccount(
			@Valid @RequestBody(required = true) AccountRegisterDto AccountRegisterDto
			) {
		log.info("Register account [{}]", AccountRegisterDto);
		
		return new ResponseDto<>(ResponseCode.ACCOUNT_REGISTER_SUCCESS, accountService.registerAccount(AccountRegisterDto));
	}

	/**
	 * Login account
	 * @param accountLoginDto
	 * @return
	 */
	@ApiOperation(value = "사용자 로그인")
	@PostMapping("/login")
	public ResponseDto<AccountLoginDto> loginAccount(
			@Valid @RequestBody(required = true) AccountLoginDto accountLoginDto
			) {
		log.info("Login account [{}]", accountLoginDto);
		
		return new ResponseDto<>(ResponseCode.ACCOUNT_LOGIN_SUCCESS, accountService.loginAccount(accountLoginDto));
	}
}
