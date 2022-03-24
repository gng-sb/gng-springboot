package com.gng.springboot.commons.exception.custom;

public class CustomException extends RuntimeException {

	private static final long serialVersionUID = -6339763157703308856L;

	public CustomException(String msg) {
		super(msg);
	}

	
	public CustomException(Exception e) {
		super(e);
	}
	
	public CustomException(String msg, Exception e) {
		super(msg, e);
	}
}
