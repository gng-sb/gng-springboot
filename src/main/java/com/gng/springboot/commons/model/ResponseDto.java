package com.gng.springboot.commons.model;

import com.gng.springboot.commons.exception.model.ResponseCode;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * DTO response for result or expected errors
 * @author gchyoo
 *
 * @param <T>
 */
@Getter
public class ResponseDto<T> extends BaseResponseDto {

	@ApiModelProperty(position=4, notes="응답 데이터")
	private T result;
	
	public ResponseDto(final ResponseCode responseCode) {
		super(responseCode);
	}
	
	public ResponseDto(final ResponseCode responseCode, T result) {
		super(responseCode);
		this.result = result;
	}
}
