package com.gng.springboot.commons.exception.model;

import lombok.Getter;

/**
 * Error response class
 * @author 유강촌
 *
 */
@Getter
public class ErrorResponse {
	
	private boolean isSuccess;
	private String messageCode;
	private String message;
	
	private ErrorResponse(final ResponseCode responseCode, final String message) {
		this.isSuccess = responseCode.isSuccess();
		this.messageCode = responseCode.getMessageCode();
		this.message = message;
	}
	
	private ErrorResponse(final boolean isSuccess, final String messageCode, final String message) {
		this.isSuccess = isSuccess;
		this.messageCode = messageCode;
		this.message = message;
	}
	
	public static ErrorResponse of(final ResponseCode responseCode, final String message) {
		return new ErrorResponse(responseCode, message);
	}
	
	public static ErrorResponse of(final boolean isSuccess, final String messageCode, final String message) {
		return new ErrorResponse(isSuccess, messageCode, message);
	}
}
