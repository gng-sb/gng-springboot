package com.gng.springboot.commons.exception.handler;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;

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

import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.custom.BusinessException;
import com.gng.springboot.commons.model.ErrorResponseDto;

import lombok.extern.slf4j.Slf4j;


/**
 * REST API exception handler
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
	@ExceptionHandler(value = {BusinessException.class})
	protected ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException bex) {
		log.error("handleBusinessException() : ", bex);
		
		return ResponseEntity.status(bex.getResponseCode().getHttpStatus())
				.body(new ErrorResponseDto(bex.getResponseCode()));
	}
	
	/**
	 * Validation error
	 * @param bindingResult
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException cex) {
		ConstraintViolation<?> violation = cex.getConstraintViolations().iterator().next();
		
		// Get the last node of the violation
		String field = null;
		for (Node node : violation.getPropertyPath()) {
			field = node.getName();
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponseDto(ResponseCode.BAD_REQUEST, extractErrorMessages(cex), field));
	}
	
	/**
	 * Unexpected error
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = {Exception.class})
	protected ResponseEntity<ErrorResponseDto> handleUnexpectedException(Exception ex) {
		log.error("handleUnexpectedException() : ", ex);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponseDto(ResponseCode.INTERNAL_SERVER_ERROR));
	}
	
	/**
	 * Bad request error
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponseDto(ResponseCode.BAD_REQUEST));
	}
	
	/**
	 * Unsupported request method error
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(new ErrorResponseDto(ResponseCode.METHOD_NOT_ALLOWED));
	}
	
	
	private String extractErrorMessages(ConstraintViolationException e) {
		return String.join("\n", e.getConstraintViolations().stream()
			.map(ConstraintViolation::getMessage)
			.collect(Collectors.toList()));
	}
}
