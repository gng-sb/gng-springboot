package com.gng.springboot.commons.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.gng.springboot.commons.constant.Constants.RoleTypes;
import com.gng.springboot.jwt.component.JwtTokenProvider;
import com.gng.springboot.jwt.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * Spring security configuration
 * @author gchyoo
 *
 */
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final JwtTokenProvider jwtTokenProvider;

	@Value("${jwt.access.valid-time}")
	private Long accessTokenValidTime;
	
	/**
	 * Register AuthenticationManager bean
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	/**
	 * CORS configuration
	 * @return
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("*");
		configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(accessTokenValidTime);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.httpBasic().disable() // Disable default config
				.csrf().disable() // Disable csrf security token
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Disable session because of token
				.and()
				.authorizeRequests()
				.antMatchers(RoleTypes.ROLE_ADMIN.getMatcher()).hasRole(RoleTypes.ROLE_ADMIN.getRole())
				.antMatchers(RoleTypes.ROLE_USER.getMatcher()).hasRole(RoleTypes.ROLE_USER.getRole())
				.antMatchers("/**").permitAll()
				.and()
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
	}
}
