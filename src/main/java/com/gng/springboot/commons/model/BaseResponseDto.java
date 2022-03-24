package com.gng.springboot.commons.model;

import com.gng.springboot.commons.constant.ResponseCode;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Base response DTO
 * @author gchyoo
 *
 */
@Setter
@Getter
public class BaseResponseDto {
	
	@ApiModelProperty(position=1, notes="응답 상태(true/false)")
	protected boolean isSuccess;

	@ApiModelProperty(position=2, notes="응답 메시지 내용")
	protected String message;
	
	protected BaseResponseDto(final ResponseCode responseCode) {
		setResponseCode(responseCode);
	}
	
	public void setResponseCode(ResponseCode responseCode) {
		this.isSuccess = responseCode.isSuccess();
		this.message = responseCode.getMessage();
	}
}
