package com.gng.springboot.commons.exception.model;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor
@Getter
public enum ResponseCode {
	
	// SUCCESS
	SUCCESS(true, HttpStatus.OK, "성공하였습니다."),
	USER_REGISTER_SUCCESS(true, HttpStatus.CREATED, "사용자 아이디 생성에 성공하였습니다."),
	LOGIN_SUCCESS(true, HttpStatus.OK, "로그인에 성공하였습니다."),
	
	// FAILURE
	BAD_REQUEST(false, HttpStatus.BAD_REQUEST, "입력 파라미터가 잘못되었습니다."),
	UNEXPECTED_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, "서버에 예기치 못한 문제가 발생하였습니다."),
	INTERNAL_SERVER(false, HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생하였습니다."),
	METHOD_NOT_ALLOWED(false, HttpStatus.METHOD_NOT_ALLOWED, "허용하지 않는 메서드입니다."),
	USER_ID_CONFLICT(false, HttpStatus.CONFLICT, "동일한 아이디를 가진 사용자가 존재합니다."),
	LOGIN_FAILURE(false, HttpStatus.BAD_REQUEST, "로그인에 실패하였습니다."),
	USER_ID_FAILURE(false, HttpStatus.BAD_REQUEST, "사용자 아이디가 존재하지 않습니다."),
	PASSWORD_FAILURE(false, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
	
	private boolean isSuccess;
	private HttpStatus httpStatus;
	private String message;
}
