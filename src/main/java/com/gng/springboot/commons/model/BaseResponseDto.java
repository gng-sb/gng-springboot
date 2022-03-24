package com.gng.springboot.commons.model;

import org.springframework.http.HttpStatus;

import com.gng.springboot.commons.exception.model.ResponseCode;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Base response class
 * @author gchyoo
 *
 */
@Setter
@Getter
public class BaseResponseDto {
	
	@ApiModelProperty(position=1, notes="응답 Http 상태")
	protected HttpStatusValue httpStatus = new HttpStatusValue(HttpStatus.OK);

	@ApiModelProperty(position=2, notes="응답 상태(true/false)")
	protected boolean isSuccess;

	@ApiModelProperty(position=3, notes="응답 메시지 내용")
	protected String message = "";
	
	protected BaseResponseDto(final ResponseCode responseCode) {
		setResponseCode(responseCode);
	}
	
	public void setResponseCode(ResponseCode responseCode) {
		this.isSuccess = responseCode.isSuccess();
		this.message = responseCode.getMessage();
		this.httpStatus = new HttpStatusValue(responseCode.getHttpStatus());
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public class HttpStatusValue {
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
