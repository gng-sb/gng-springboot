package com.gng.springboot.commons.exception.custom;

import com.gng.springboot.commons.exception.model.ResponseCode;

import lombok.Getter;

/**
 * Business exception class
 * @author gchyoo
 *
 */
@Getter
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 4756502529844995804L;
	
	private ResponseCode responseCode;

	public BusinessException(ResponseCode responseCode) {
		super(responseCode.getMessage());
		this.responseCode = responseCode;
	}

	public BusinessException(ResponseCode responseCode, String message) {
		super(message);
		this.responseCode = responseCode;
	}
}
