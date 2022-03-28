package com.gng.springboot.commons.model;

import com.gng.springboot.commons.constant.ResponseCode;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * Error response DTO
 * @author gchyoo
 *
 */
@Getter
public class ErrorResponseDto extends BaseResponseDto {

	@ApiModelProperty(position=3, notes="오류 발생 필드")
	private String errorField;
	
	public ErrorResponseDto(final ResponseCode responseCode) {
		super(responseCode);
	}

	public ErrorResponseDto(final ResponseCode responseCode, final String message) {
		super(responseCode, message);
	}

	public ErrorResponseDto(final ResponseCode responseCode, final String message, final String errorField) {
		super(responseCode, message);
		this.errorField = errorField;
	}
}
