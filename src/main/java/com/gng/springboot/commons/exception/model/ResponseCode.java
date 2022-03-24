package com.gng.springboot.commons.exception.model;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ResponseCode {
	
	// SUCCESS
	SUCCESS(true, HttpStatus.OK, "success"),
	CREATE_SUCCESS(true, HttpStatus.CREATED, "createSuccess"),
	LOGIN_SUCCESS(true, HttpStatus.OK, "loginSuccess"),
	
	// FAILURE
	FAILURE(false, HttpStatus.INTERNAL_SERVER_ERROR, "failure"),
	CREATE_FAILURE(false, HttpStatus.CONFLICT, "createFailure"),
	LOGIN_FAILURE(false, HttpStatus.BAD_REQUEST, "loginFailure"),
	BAD_REQUEST(false, HttpStatus.BAD_REQUEST, "badRequest"),
	INTERNAL_SERVER(false, HttpStatus.INTERNAL_SERVER_ERROR, "internalServer"),
	METHOD_NOT_ALLOWED(false, HttpStatus.METHOD_NOT_ALLOWED, "methodNotAllowed"),
	USER_AUTHENTICATION(false, HttpStatus.UNAUTHORIZED, "userAuthentication");
	
	private final boolean isSuccess;
	private final HttpStatus httpStatus;
	private final String messageCode;
}
