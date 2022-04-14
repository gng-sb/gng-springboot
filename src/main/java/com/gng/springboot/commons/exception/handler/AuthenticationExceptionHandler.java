package com.gng.springboot.commons.exception.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.model.ErrorResponseDto;

/**
 * Handle web security exception
 * @author gchyoo
 *
 */
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

	/**
	 * Handle jwt exception
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.setStatus(ResponseCode.FILTER_TOKEN_INVALID.getHttpStatus().value());
		
		OutputStream out = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(out, new ErrorResponseDto(ResponseCode.FILTER_TOKEN_INVALID));
		out.flush();
	}
	
}
