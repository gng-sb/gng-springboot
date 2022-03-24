package com.gng.springboot.commons.model;

import org.springframework.http.HttpStatus;

import com.gng.springboot.commons.exception.model.ResponseCode;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

	@ApiModelProperty(position=3, notes="응답 Http 상태")
	private HttpStatusValue httpStatus = new HttpStatusValue(HttpStatus.OK);

	@ApiModelProperty(position=4, notes="응답 메시지 내용")
	private String message = "";
	
	@ApiModelProperty(position=5, notes="응답 데이터")
	private T result;

	public void set(ResponseCode responseCode) {
		this.setResponseCode(responseCode);
	}

	public void set(ResponseCode responseCode, T result) {
		this.setResponseCode(responseCode);
		this.result = result;
	}

	public void set(ResponseCode responseCode, Exception ex) {
		this.setResponseCode(responseCode);
		this.message = ex.getMessage();
	}
	
	public void setMessage(ResponseCode responseCode, String message) {
		this.setResponseCode(responseCode);
		this.message = message;
	}
	
	public void setResponseCode(ResponseCode responseCode) {
		this.isSuccess = responseCode.isSuccess();
		this.messageCode = responseCode.getMessageCode();
		this.httpStatus = new HttpStatusValue(responseCode.getHttpStatus());
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class HttpStatusValue {
		private String name;
		private int code;
		private String reason;
		
		public HttpStatusValue(HttpStatus httpStatus) {
			this.name = httpStatus.name();
			this.code = httpStatus.value();
			this.reason = httpStatus.getReasonPhrase();
		}
	}
}
