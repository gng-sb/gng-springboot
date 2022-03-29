package com.gng.springboot.commons.model;

import java.util.List;

import javax.persistence.Transient;

import org.springframework.http.HttpStatus;

import com.gng.springboot.commons.constant.ResponseCode;
import com.google.common.collect.Lists;

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
	
	@Transient
	protected HttpStatus httpStatus;
	
	@ApiModelProperty(position=1, notes="응답 상태(true/false)")
	protected boolean isSuccess = true;
	
	@ApiModelProperty(position=2, notes="응답 메시지 내용")
	protected List<String> messages = Lists.newArrayList();

	protected BaseResponseDto(final ResponseCode responseCode) {
		setResponseCode(responseCode);
	}

	protected BaseResponseDto(final ResponseCode responseCode, final String message) {
		setResponseCode(responseCode, message);
	}
	
	protected BaseResponseDto(final ResponseCode responseCode, final List<String> messages) {
		setResponseCode(responseCode, messages);
	}

	public void setResponseCode(final ResponseCode responseCode) {
		this.httpStatus = responseCode.getHttpStatus();
		this.isSuccess = responseCode.isSuccess();
		this.messages.add(responseCode.getMessage());
	}

	public void setResponseCode(final ResponseCode responseCode, final String message) {
		this.httpStatus = responseCode.getHttpStatus();
		this.isSuccess = responseCode.isSuccess();
		this.messages.add(message);
	}
	
	public void setResponseCode(final ResponseCode responseCode, final List<String> messages) {
		this.httpStatus = responseCode.getHttpStatus();
		this.isSuccess = responseCode.isSuccess();
		this.messages = messages;
	}
}
