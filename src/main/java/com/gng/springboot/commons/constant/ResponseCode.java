package com.gng.springboot.commons.constant;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Success/Failure enum
 * @author gchyoo
 *
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor
@Getter
public enum ResponseCode {
	
	// SUCCESS
	SUCCESS(true, HttpStatus.OK, "성공하였습니다."),
	ACCOUNT_REGISTER_SUCCESS(true, HttpStatus.CREATED, "사용자 아이디 생성에 성공하였습니다. 가입 시 사용한 이메일을 통해 계정 인증을 완료해주세요."),
	EMAIL_TOKEN_CONFIRM_SUCCESS(true, HttpStatus.OK, "이메일 인증에 성공하였습니다."),
	ACCOUNT_LOGIN_SUCCESS(true, HttpStatus.OK, "로그인에 성공하였습니다."),
	
	// FAILURE
	BAD_REQUEST(false, HttpStatus.BAD_REQUEST, "입력 파라미터가 잘못되었습니다."),
	UNEXPECTED_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, "서버에 예기치 못한 문제가 발생하였습니다."),
	INTERNAL_SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, "서버에 예기치 못한 문제가 발생하였습니다."),
	METHOD_NOT_ALLOWED(false, HttpStatus.METHOD_NOT_ALLOWED, "허용하지 않는 메서드입니다."),
	
	ACCOUNT_ID_CONFLICT(false, HttpStatus.CONFLICT, "동일한 아이디를 가진 사용자가 존재합니다."),
	ACCOUNT_NOT_FOUND(false, HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다"),
	ACCOUNT_NOT_EXIST(false, HttpStatus.BAD_REQUEST, "사용자 아이디가 존재하지 않습니다."),
	ACCOUNT_LOGIN_FAILURE(false, HttpStatus.BAD_REQUEST, "로그인에 실패하였습니다."),
	ACCOUNT_LOGIN_PASSWORD_FAILURE(false, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
	
	EMAIL_TOKEN_NOT_FOUND(false, HttpStatus.NOT_FOUND, "유효한 토큰을 찾을 수 없습니다."),
	EMAIL_TOKEN_SEND_FAILURE(false, HttpStatus.INTERNAL_SERVER_ERROR, "인증 메일 전송 도중 오류가 발생하였습니다.")
	;
	
	private boolean isSuccess;
	private HttpStatus httpStatus;
	private String message;
}
