package com.gng.springboot.jwt.component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.gng.springboot.commons.constant.Constants.UserRoles;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT token provider class
 * @author gchyoo
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider{
	
	private final UserDetailsService userDetailService;
	
	@Value("${jwt.password}")
	private String jwtKey;
	private String jwtBase64Key;
	
	// 토큰 유효시간
	private Long tokenValidTime = 30 * 60 * 1000L;
	
	@PostConstruct
	public void init() {
		log.debug("Initialize JwtTokenProvider ...");
		
		jwtBase64Key = Base64.getEncoder().encodeToString(jwtKey.getBytes());
	}
	
	/**
	 * Create token
	 * @param userPk
	 * @param roles
	 * @return
	 */
	public String createToken(String userPk, List<UserRoles> roles) {
		log.debug("Create token [userPk={}] [roles={}]", userPk, roles);
		
		Claims claims = Jwts.claims().setSubject(userPk);
		
		claims.put("roles", roles);
		
		Date now = new Date();
		
		return Jwts.builder()
				.setClaims(claims) // Save data
				.setIssuedAt(now) // Token creation time
				.setExpiration(new Date(now.getTime() + tokenValidTime)) // Expire time
				.signWith(SignatureAlgorithm.HS256, jwtBase64Key)
				.compact();
	}
	
	/**
	 * Get authentication data from JWT token
	 * @param token
	 * @return
	 */
	public Authentication getAuthentication(String token) {
		log.debug("Get authentication from [token={}]", token);
		
		UserDetails userDetails = userDetailService.loadUserByUsername(this.getUserPk(token));
		
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	/**
	 * Get user data from token
	 * @param token
	 * @return
	 */
	public String getUserPk(String token) {
		log.debug("Get user PK from [token={}]", token);
		
		return Jwts.parser()
				.setSigningKey(jwtBase64Key)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	/**
	 * Get token from header of request("X-AUTH-TOKEN": "TOKEN value")
	 * @param request
	 * @return
	 */
	public String resolveToken(HttpServletRequest request) {
		log.debug("Resolve token [request={}]", request);
		
		return request.getHeader("X-AUTH-TOKEN");
	}
	
	/**
	 * Check token is valid(Validation + Expiration)
	 * @param token
	 * @return
	 */
	public boolean isTokenValid(String token) {
		log.debug("Check token is valid [token={}]", token);
		
		try {
			Jws<Claims> claims = Jwts.parser()
					.setSigningKey(jwtBase64Key)
					.parseClaimsJws(token);
			
			log.debug("Valid token [token={}]", token);
			
			return !claims.getBody()
					.getExpiration()
					.before(new Date());
		} catch(Exception ex) {
			log.debug("Invalid token [token={}]", token);
			
			return false;
		}
	}
	
}
