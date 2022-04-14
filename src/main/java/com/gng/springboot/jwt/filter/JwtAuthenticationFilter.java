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
		if(token != null && jwtTokenProvider.isTokenValid(token, JwtTokenProvider.X_AUTH_ACCESS_TOKEN)) {
			// Get user data from token
			Authentication authentication = jwtTokenProvider.getAuthentication(token, JwtTokenProvider.X_AUTH_ACCESS_TOKEN);
			
			// Store authentication into SecurityContext
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);
		}
		
		chain.doFilter(request, response);
	}
	
}
