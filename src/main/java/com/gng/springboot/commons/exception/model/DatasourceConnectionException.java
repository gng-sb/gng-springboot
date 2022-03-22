package com.gng.springboot.commons.exception.model;

public class DatasourceConnectionException extends Exception {
	
	private static final long serialVersionUID = 2792341148818000401L;

	public DatasourceConnectionException(String msg) {
		super(msg);
	}

	
	public DatasourceConnectionException(Exception e) {
		super(e);
	}
	
	public DatasourceConnectionException(String msg, Exception e) {
		super(msg, e);
	}
}
