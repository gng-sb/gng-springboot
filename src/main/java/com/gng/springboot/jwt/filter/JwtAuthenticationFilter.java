package com.gng.springboot.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gng.springboot.commons.exception.custom.BusinessException;
import com.gng.springboot.jwt.component.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Get token from header
		String token = jwtTokenProvider.resolveToken(request, JwtTokenProvider.X_AUTH_ACCESS_TOKEN);
		
		// Check token is valid
		try {
			if(token != null && jwtTokenProvider.isTokenValid(token)) {
				// Get user data from token
				Authentication authentication = jwtTokenProvider.getAuthentication(token);
				
				// Store authentication into SecurityContext
				SecurityContextHolder.getContext()
						.setAuthentication(authentication);
			}
		} catch(BusinessException bex) {
			System.out.println("TEST");
			response.sendError(bex.getResponseCode().getHttpStatus().value(), bex.getResponseCode().getMessage());
		} catch(Exception ex) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		// TODO: 필터 예외를 403으로 리턴해서 클라이언트쪽에서 토큰 재발급 후 이전 요청을 재전송하도록 해야함
		/**
		 *  할일
		 *  1. 커스텀 예외 응답 403으로 던질것
		 *  2. 프론트에서 예외 403 확인해서 POST /jwt/token에
		 *  	access/refresh 토큰 담아서 요청 던질것(컨트롤러 만들어야됨)
		 *  	access/refresh 유효한 토큰인지(잘못된 토큰이 아닌지) 다시 확인해야됨
		 *  3. 유효한 토큰이면 
		 */
		// TODO: refresh token이 유효한지도 확인해야함
		// TODO: 
		
		chain.doFilter(request, response);
	}
}
