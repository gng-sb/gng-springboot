package com.gng.springboot.commons.model;

import com.gng.springboot.commons.constant.ResponseCode;

import lombok.Getter;

/**
 * Error response DTO
 * @author gchyoo
 *
 */
@Getter
public class ErrorResponseDto extends BaseResponseDto {

	public ErrorResponseDto(final ResponseCode responseCode) {
		super(responseCode);
	}
}
