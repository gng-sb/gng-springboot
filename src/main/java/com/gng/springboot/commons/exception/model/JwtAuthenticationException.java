package com.gng.springboot.commons.exception.model;

public class JwtAuthenticationException extends RuntimeException {

	private static final long serialVersionUID = -4770451607361754471L;

	public JwtAuthenticationException(String msg) {
		super(msg);
	}

	
	public JwtAuthenticationException(Exception e) {
		super(e);
	}
	
	public JwtAuthenticationException(String msg, Exception e) {
		super(msg, e);
	}
}
