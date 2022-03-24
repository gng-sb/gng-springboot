package com.gng.springboot.commons.exception.model;

import com.gng.springboot.commons.model.BaseResponseDto;

import lombok.Getter;

/**
 * Error response class
 * @author gchyoo
 *
 */
@Getter
public class ErrorResponseDto extends BaseResponseDto {

	public ErrorResponseDto(final ResponseCode responseCode) {
		super(responseCode);
	}
}
