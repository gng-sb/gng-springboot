package com.gng.springboot.commons.model;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.gng.springboot.commons.exception.model.ResponseCode;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO response for result or expected errors
 * @author gchyoo
 *
 * @param <T>
 */
@Getter
@Setter
@NoArgsConstructor
public class ResponseDto<T> {

	@ApiModelProperty(position=1, notes="응답 상태(true/false)")
	private boolean isSuccess;
	
	@ApiModelProperty(position=2, notes="응답 메시지 코드")
	private String messageCode;
	
	@ApiModelProperty(position=3, notes="응답 메시지 내용")
	private String message = HttpStatus.OK.name();

	@ApiModelProperty(position=4, notes="응답 데이터")
	private T result;

	public void set(ResponseCode responseCode) {
		this.isSuccess = responseCode.isSuccess();
		this.messageCode = responseCode.getMessageCode();
	}

	public void set(ResponseCode responseCode, T result) {
		this.isSuccess = responseCode.isSuccess();
		this.messageCode = responseCode.getMessageCode();
		this.result = result;
	}
	
	public void setMessage(ResponseCode responseCode, String message) {
		this.isSuccess = responseCode.isSuccess();
		this.messageCode = responseCode.getMessageCode();
		this.message = message;
	}
}
