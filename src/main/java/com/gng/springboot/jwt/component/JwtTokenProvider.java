package com.gng.springboot.jwt.component;

import java.util.Base64;
import java.util.Date;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
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
	
	@Value("${jwt.access.valid-time}")
	private Long ACCESS_TOKEN_VALID_TIME;
	@Value("${jwt.refresh.valid-time}")
	private Long REFRESH_TOKEN_VALID_TIME;

	@Value("${jwt.access.password}")
	private String ACCESS_TOKEN_KEY;
	@Value("${jwt.refresh.password}")
	private String REFRESH_TOKEN_KEY;
	
	public static final String X_AUTH_ACCESS_TOKEN = "X-AUTH-TOKEN";
	public static final String X_AUTH_REFRESH_TOKEN = "X-AUTH-REFRESH-TOKEN";
	
	private String accessTokenBase64Key;
	private String refreshTokenBase64Key;
	
	@PostConstruct
	public void init() {
		log.debug("Initialize JwtTokenProvider ...");
		
		accessTokenBase64Key = Base64.getEncoder().encodeToString(ACCESS_TOKEN_KEY.getBytes());
		refreshTokenBase64Key = Base64.getEncoder().encodeToString(REFRESH_TOKEN_KEY.getBytes());
	}
	
	/**
	 * Create access token
	 * @param userPk
	 * @param roles
	 * @return access token
	 */
	public String createAccessToken(String userPk, Set<String> roleTypeSet) {
		log.debug("Create access token [userPk={}] [roleTypeSet={}]", userPk, roleTypeSet);
		
		Claims claims = Jwts.claims().setSubject(userPk);
		claims.put("roles", roleTypeSet);
		
		Date now = new Date();
		Date expirationDate = new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME * 1000);
		
		return buildJwt(claims, now, expirationDate, accessTokenBase64Key);
	}
	
	
	/**
	 * Create refresh token
	 * @param uuid key for refresh token
	 * @return refresh token
	 */
	public String createRefreshToken(String uuid) {
		log.debug("Create refresh token [uuid={}]", uuid);
		
		Claims claims = Jwts.claims();
		claims.put("uuid", uuid);
		
		Date now = new Date();
		Date expirationDate = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME * 1000);
		
		return buildJwt(claims, now, expirationDate, refreshTokenBase64Key);
	}
	
	/**
	 * Build token
	 * @param claims
	 * @param now
	 * @param expirationDate
	 * @return token
	 */
	public String buildJwt(Claims claims, Date now, Date expirationDate, String tokenBase64Key) {
		log.debug("[{}] [{}]", ACCESS_TOKEN_VALID_TIME, REFRESH_TOKEN_VALID_TIME);
		log.debug(now.toString());
		log.debug(expirationDate.toString());
		return Jwts.builder()
				.setClaims(claims) // Save data
				.setIssuedAt(now) // Token creation time
				.setExpiration(expirationDate) // Expire time
				.signWith(SignatureAlgorithm.HS256, tokenBase64Key)
				.compact();
	}
	
	/**
	 * Get token from header of request("X-AUTH-(REFRESH)-TOKEN": "TOKEN value")
	 * @param request
	 * @param tokenType JwtTokenProvider.X_AUTH_ACCESS_TOKEN / JwtTokenProvider.X_AUTH_REFRESH_TOKEN
	 * @return token
	 */
	public String resolveToken(HttpServletRequest request, String tokenType) {
		log.debug("Resolve token [tokenType={}]", tokenType);
		
		return request.getHeader(tokenType);
	}
	
	/**
	 * Get authentication data from JWT token
	 * @param token
	 * @return
	 */
	public Authentication getAuthentication(String token, String tokenType) {
		log.debug("Get authentication");
		
		UserDetails userDetails = userDetailService.loadUserByUsername(this.getUserId(token, getTokenKey(tokenType)));
		
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	/**
	 * Get user data from token
	 * @param token
	 * @return
	 */
	public String getUserId(String token, String tokenType) {
		log.debug("Get user ID");

		return getClaimsFromToken(token, tokenType)
				.getBody()
				.getSubject();
	}
	
	/**
	 * Check token is valid(Validation + Expiration)
	 * @param token
	 * @return
	 */
	public boolean isTokenValid(String token, String tokenType) {
		log.debug("Check token is valid");
		
		try {
			Jws<Claims> claims = getClaimsFromToken(token, tokenType);
			
			log.debug("Valid token");
			
			return !claims.getBody()
					.getExpiration()
					.before(new Date());
		} catch(SignatureException sigex) {
			log.error("Invalid JWT signature");
		} catch(MalformedJwtException malex) {
			log.error("Invalid token");
		} catch(ExpiredJwtException expex) {
			log.error("Expired token", expex);
		} catch(UnsupportedJwtException unex) {
			log.error("Unsupported token");
		} catch(IllegalArgumentException ilex) {
			log.error("Empty token");
		} catch(Exception ex) {
			log.error("Exception occurred in isTokenValid()", ex);
		}
		
		return false;
	}
	
	/**
	 * Get claims from token
	 * @param token jwt
	 * @param tokenType token key type
	 * @return claims
	 */
	public Jws<Claims> getClaimsFromToken(String token, String tokenType) {
		return Jwts.parser()
				.setSigningKey(getTokenKey(tokenType))
				.parseClaimsJws(token);
	}
	
	public String getTokenKey(String tokenType) {
		if(tokenType.equals(X_AUTH_ACCESS_TOKEN)) {
			return accessTokenBase64Key;
		} else if(tokenType.equals(X_AUTH_REFRESH_TOKEN)) {
			return refreshTokenBase64Key;
		} else {
			return tokenType;
		}
	}
}
