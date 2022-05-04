package com.gng.springboot.account.controller;

import javax.validation.Valid;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gng.springboot.account.model.AccountLoginDto;
import com.gng.springboot.account.model.AccountRegisterDto;
import com.gng.springboot.account.service.AccountService;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.model.ResponseDto;
import com.gng.springboot.email.service.EmailConfirmService;

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
	private final AccountService accountServiceImpl;
	private final EmailConfirmService emailConfirmServiceImpl;
	
	@ApiOperation(
			value = "계정 등록",
			notes = "사용자가 입력한 정보로 계정 등록과 인증 메일 전송을 수행한다."
			)
	@PostMapping(value = "/register", produces = {MediaTypes.HAL_JSON_VALUE})
	public ResponseEntity<ResponseDto<String>> accountRegister(
			@Valid @RequestBody(required = true) AccountRegisterDto accountRegisterDto
			) {
		log.info("Register account [{}]", accountRegisterDto);
		
		ResponseDto<String> responseDto = new ResponseDto<>(ResponseCode.ACCOUNT_REGISTER_SUCCESS, accountServiceImpl.accountRegister(accountRegisterDto));

		// Send confirmation mail
		emailConfirmServiceImpl.sendEmailConfirmToken(accountRegisterDto.getId());
		
		return ResponseEntity.status(responseDto.getHttpStatus())
				.body(responseDto);
	}

	@ApiOperation(
			value = "계정 로그인",
			notes = "사용자가 입력한 ID/PW로 로그인을 수행한다."
			)
	@PostMapping(value = "/login", produces = {MediaTypes.HAL_JSON_VALUE})
	public ResponseEntity<ResponseDto<AccountLoginDto>> accountLogin(
			@Valid @RequestBody(required = true) AccountLoginDto accountLoginDto
			) {
		log.info("Login account [{}]", accountLoginDto);

		ResponseDto<AccountLoginDto> responseDto = new ResponseDto<>(ResponseCode.ACCOUNT_LOGIN_SUCCESS, accountServiceImpl.accountLogin(accountLoginDto));

		return ResponseEntity.status(responseDto.getHttpStatus())
				.body(responseDto);
	}
}
