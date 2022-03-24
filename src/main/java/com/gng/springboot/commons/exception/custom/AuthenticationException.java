package com.gng.springboot.commons.exception.custom;

public class AuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 4756502529844995804L;

	public AuthenticationException(String msg) {
		super(msg);
	}

	
	public AuthenticationException(Exception e) {
		super(e);
	}
	
	public AuthenticationException(String msg, Exception e) {
		super(msg, e);
	}
}
