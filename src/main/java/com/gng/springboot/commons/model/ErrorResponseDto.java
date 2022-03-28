package com.gng.springboot.commons.model;

import java.util.List;

import com.gng.springboot.commons.constant.ResponseCode;
import com.google.common.collect.Lists;

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
	private List<String> errorFields = Lists.newArrayList();
	
	public ErrorResponseDto(final ResponseCode responseCode) {
		super(responseCode);
	}

	public ErrorResponseDto(final ResponseCode responseCode, final List<String> message) {
		super(responseCode, message);
	}

	public ErrorResponseDto(final ResponseCode responseCode, final List<String> message, final List<String> errorFields) {
		super(responseCode, message);
		this.errorFields = errorFields;
	}
}
