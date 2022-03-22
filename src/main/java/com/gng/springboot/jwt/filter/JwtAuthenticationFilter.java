package com.gng.springboot.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.gng.springboot.jwt.component.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Get token from header
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
		
		// Check token is valid
		if(token != null && jwtTokenProvider.isTokenValid(token)) {
			// Get user data from token
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			
			// Store authentication into SecurityContext
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);
		}
		
		chain.doFilter(request, response);
	}
}
