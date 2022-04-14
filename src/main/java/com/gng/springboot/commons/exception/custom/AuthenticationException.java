package com.gng.springboot.commons.exception.custom;

import com.gng.springboot.commons.constant.ResponseCode;

import lombok.Getter;

/**
 * Authentication custom exception
 * @author gchyoo
 *
 */
@Getter
public class AuthenticationException extends RuntimeException {

	private static final long serialVersionUID = -5042521016877503238L;
	
	private ResponseCode responseCode;

	public AuthenticationException(ResponseCode responseCode) {
		super(responseCode.getMessage());
		this.responseCode = responseCode;
	}

	public AuthenticationException(ResponseCode responseCode, String message) {
		super(message);
		this.responseCode = responseCode;
	}
}
