package com.gng.springboot.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gng.springboot.commons.constant.Constants.UserRoles;
import com.gng.springboot.jwt.component.JwtTokenProvider;
import com.gng.springboot.jwt.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final JwtTokenProvider jwtTokenProvider;
	
	/**
	 * Register PasswordEncoder bean for encryption
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	/**
	 * Register AuthenticationManager bean
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.httpBasic().disable() // Disable default config
				.csrf().disable() // Disable csrf security token
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Disable session because of token
				.and()
				.authorizeRequests()
				.antMatchers(UserRoles.ADMIN.getMatcher()).hasRole(UserRoles.ADMIN.getRole())
				.antMatchers(UserRoles.USER.getMatcher()).hasRole(UserRoles.USER.getRole())
				.antMatchers("/**").permitAll()
				.and()
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
	}
}
