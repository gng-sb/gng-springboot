package com.gng.springboot.commons.exception.custom;

public class UserExistException extends RuntimeException {

	private static final long serialVersionUID = 8797946770265400900L;

	public UserExistException(String msg) {
		super(msg);
	}

	
	public UserExistException(Exception e) {
		super(e);
	}
	
	public UserExistException(String msg, Exception e) {
		super(msg, e);
	}
}
