package com.gng.springboot.commons.model;

import com.gng.springboot.commons.constant.ResponseCode;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * Response DTO
 * @author gchyoo
 *
 * @param <T>
 */
@Getter
public class ResponseDto<T> extends BaseResponseDto {

	@ApiModelProperty(position=3, notes="응답 데이터")
	private T result;
	
	public ResponseDto(final ResponseCode responseCode) {
		super(responseCode);
	}
	
	public ResponseDto(final ResponseCode responseCode, T result) {
		super(responseCode);
		this.result = result;
	}
}
