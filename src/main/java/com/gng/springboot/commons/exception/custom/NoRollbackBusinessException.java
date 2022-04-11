package com.gng.springboot.commons.exception.custom;

import com.gng.springboot.commons.constant.ResponseCode;

import lombok.Getter;

/**
 * Business custom exception with no rollback
 * @author gchyoo
 *
 */
@Getter
public class NoRollbackBusinessException extends RuntimeException {
	
	private static final long serialVersionUID = -5138489623169567424L;
	
	private ResponseCode responseCode;

	public NoRollbackBusinessException(ResponseCode responseCode) {
		super(responseCode.getMessage());
		this.responseCode = responseCode;
	}

	public NoRollbackBusinessException(ResponseCode responseCode, String message) {
		super(message);
		this.responseCode = responseCode;
	}
}
