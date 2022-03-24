package com.gng.springboot.commons.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.gng.springboot.commons.exception.custom.BusinessException;
import com.gng.springboot.commons.exception.model.ErrorResponseDto;
import com.gng.springboot.commons.exception.model.ResponseCode;

import lombok.extern.slf4j.Slf4j;


/**
 * REST API exception handler class
 * @author gchyoo
 *
 */
@Slf4j
@RestControllerAdvice
public class RestAPIExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Business error
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({BusinessException.class})
	protected ErrorResponseDto handleBusinessException(BusinessException bex) {
		log.error("handleBusinessException() : ", bex);
		
		return new ErrorResponseDto(bex.getResponseCode());
	}
	
	/**
	 * Unexpected error
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({Exception.class})
	protected ErrorResponseDto handleUnexpectedException(Exception ex) {
		log.error("handleUnexpectedException() : ", ex);
		
		return new ErrorResponseDto(ResponseCode.INTERNAL_SERVER);
	}
	
	/**
	 * Bad request error
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<>(new ErrorResponseDto(ResponseCode.BAD_REQUEST), HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Unsupported request method error
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<>(new ErrorResponseDto(ResponseCode.METHOD_NOT_ALLOWED), HttpStatus.METHOD_NOT_ALLOWED);
	}
	
}
