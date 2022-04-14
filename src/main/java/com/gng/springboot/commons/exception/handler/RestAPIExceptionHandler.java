package com.gng.springboot.commons.exception.handler;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.custom.BusinessException;
import com.gng.springboot.commons.exception.custom.NoRollbackBusinessException;
import com.gng.springboot.commons.model.ErrorResponseDto;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;


/**
 * Handle REST API exception
 * @author gchyoo
 *
 */
@Slf4j
@RestControllerAdvice
public class RestAPIExceptionHandler extends ResponseEntityExceptionHandler {
	
	/**
	 * Business error
	 * @param bex
	 * @return
	 */
	@ExceptionHandler(value = {BusinessException.class})
	protected ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException bex) {
		log.error("handleBusinessException() : ", bex);
		
		return ResponseEntity.status(bex.getResponseCode().getHttpStatus())
				.body(new ErrorResponseDto(bex.getResponseCode()));
	}
	
	/**
	 * Business error with no rollback
	 * @param bex
	 * @return
	 */
	@ExceptionHandler(value = {NoRollbackBusinessException.class})
	protected ResponseEntity<ErrorResponseDto> handleNoRollbackBusinessException(NoRollbackBusinessException nrbex) {
		log.error("handleNoRollbackBusinessException.class() : ", nrbex);
		
		return ResponseEntity.status(nrbex.getResponseCode().getHttpStatus())
				.body(new ErrorResponseDto(nrbex.getResponseCode()));
	}
	
	/**
	 * Validation error(Repository)
	 * @param cex
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException cex) {
		log.error("handleConstraintViolationException() : ", cex);
		
		List<String> errorFields = Lists.newArrayList();
		
		cex.getConstraintViolations().forEach(constraintViolation -> {
			constraintViolation.getPropertyPath().forEach(node -> errorFields.add(node.getName()));
		});
		
		return ResponseEntity.status(ResponseCode.BAD_REQUEST.getHttpStatus())
				.body(new ErrorResponseDto(ResponseCode.BAD_REQUEST, extractErrorMessages(cex), errorFields));
	}
	
	/**
	 * Unexpected error
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = {Exception.class})
	protected ResponseEntity<ErrorResponseDto> handleUnexpectedException(Exception ex) {
		log.error("handleUnexpectedException() : ", ex);
		
		return ResponseEntity.status(ResponseCode.INTERNAL_SERVER_ERROR.getHttpStatus())
				.body(new ErrorResponseDto(ResponseCode.INTERNAL_SERVER_ERROR));
	}
	
	/**
	 * Argument not valid error
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("handleMethodArgumentNotValidException() : ", ex);
		
		List<String> errorFields = Lists.newArrayList();
		
		ex.getBindingResult().getFieldErrors()
				.forEach(fieldError -> errorFields.add(fieldError.getField()));
		
		return ResponseEntity.status(status)
				.body(new ErrorResponseDto(ResponseCode.BAD_REQUEST, extractErrormessages(ex), errorFields));
	}

	/**
	 * Bad request error
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("handleHttpMessageNotReadable() : ", ex);
		
		return ResponseEntity.status(status)
				.body(new ErrorResponseDto(ResponseCode.BAD_REQUEST));
	}
	
	/**
	 * Unsupported request method error
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("handleHttpRequestMethodNotSupported() : ", ex);
		
		return ResponseEntity.status(status)
				.body(new ErrorResponseDto(ResponseCode.METHOD_NOT_ALLOWED));
	}
	
	private List<String> extractErrorMessages(ConstraintViolationException cex) {
		return cex.getConstraintViolations().stream()
			.map(ConstraintViolation::getMessage)
			.collect(Collectors.toList());
	}
	
	private List<String> extractErrormessages(BindingResult bindingResult) {
		return bindingResult.getFieldErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.toList());
	}
}
