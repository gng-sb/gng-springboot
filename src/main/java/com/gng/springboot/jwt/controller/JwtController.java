package com.gng.springboot.jwt.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.model.ResponseDto;
import com.gng.springboot.jwt.model.JwtRefreshDto;
import com.gng.springboot.jwt.service.JwtService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * JWT controller
 * 
 * </pre>
 * @author gchyoo
 *
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/jwt")
@RestController
public class JwtController {
	
	private final JwtService jwtService;

	@ApiOperation(
			value = "JWT 재발급",
			notes = "사용자의 Access token, Refresh token으로 JWT 재발급을 수행한다."
			)
	@PostMapping("")
	public ResponseEntity<ResponseDto<JwtRefreshDto>> refreshToken(
			@Valid @RequestBody(required = true) JwtRefreshDto jwtRefreshDto
			) {
		log.info("Refresh JWT [{}]", jwtRefreshDto);
		
		ResponseDto<JwtRefreshDto> responseDto = new ResponseDto<>(ResponseCode.JWT_REFRESH_SUCCESS, jwtService.refreshToken(jwtRefreshDto));
		
		return ResponseEntity.status(responseDto.getHttpStatus())
				.body(responseDto);
	}
	
}
