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

import com.gng.springboot.commons.exception.model.CustomException;
import com.gng.springboot.commons.exception.model.ErrorResponse;
import com.gng.springboot.commons.exception.model.JwtAuthenticationException;
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
	 * Custom error
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({CustomException.class})
	protected ErrorResponse handleCustomException(Exception ex) {
		log.error("handleCustomException() : ", ex);
		
		return ErrorResponse.of(ResponseCode.INTERNAL_SERVER, ex.getMessage());
	}

	/**
	 * Jwt authentication error
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({JwtAuthenticationException.class})
	protected ErrorResponse handleJwtAuthenticationException(Exception ex) {
		log.error("handleJwtAuthenticationException() : ", ex);
		
		return ErrorResponse.of(ResponseCode.USER_AUTHENTICATION, ex.getMessage());
	}
	
	/**
	 * Unexpected error
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({Exception.class})
	protected ErrorResponse handleException(Exception ex) {
		log.error("handleException() : ", ex);
		
		return ErrorResponse.of(ResponseCode.INTERNAL_SERVER, ex.getMessage());
	}
	
	/**
	 * Bad request error
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<>(ErrorResponse.of(ResponseCode.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Unsupported request method error
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<>(ErrorResponse.of(ResponseCode.METHOD_NOT_ALLOWED, ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
	}
	
}
