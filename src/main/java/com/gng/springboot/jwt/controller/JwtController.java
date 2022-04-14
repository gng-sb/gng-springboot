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

	// TODO: 필터 예외를 401로 리턴해서 클라이언트쪽에서 토큰 재발급 후 이전 요청을 재전송하도록 해야함
	/**
	 *  할일
	 *  1. 커스텀 예외 응답 401으로 던질것
	 *  2. 프론트에서 예외 401 확인해서 POST /jwt/token에
	 *  	access/refresh 토큰 담아서 요청 던질것(컨트롤러 만들어야됨)
	 *  	access/refresh 유효한 토큰인지(잘못된 토큰이 아닌지) 다시 확인해야됨
	 *  3. 유효한 토큰이면 
	 */

	@ApiOperation(
			value = "JWT 재발급",
			notes = "사용자의 Access token, Refresh token으로 JWT 재발급을 수행한다."
			)
	@PostMapping("")
	public ResponseEntity<ResponseDto<JwtRefreshDto>> accountRegister(
			@Valid @RequestBody(required = true) JwtRefreshDto jwtRefreshDto
			) {
		log.info("Refresh JWT [{}]", jwtRefreshDto);
		
		ResponseDto<JwtRefreshDto> responseDto = new ResponseDto<>(ResponseCode.JWT_REFRESH_SUCCESS, jwtService.refreshToken(jwtRefreshDto));
		
		return ResponseEntity.status(responseDto.getHttpStatus())
				.body(responseDto);
	}
	
}
