package com.gng.springboot.commons.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ResponseCode {
	
	// SUCCESS
	SUCCESS(true, "success"),
	CREATE_SUCCESS(true, "createSuccess"),
	
	// FAILURE
	FAILURE(false, "failure"),
	CREATE_FAILURE(false, "createFailure"),
	BAD_REQUEST(false, "badRequest"),
	INTERNAL_SERVER(false, "internalServer"),
	METHOD_NOT_ALLOWED(false, "methodNotAllowed"),
	USER_AUTHENTICATION(false, "userAuthentication");
	
	private final boolean isSuccess;
	private final String messageCode;
}
