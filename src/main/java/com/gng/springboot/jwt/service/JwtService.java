package com.gng.springboot.jwt.service;

import org.springframework.stereotype.Service;

import com.gng.springboot.account.model.AccountEntity;
import com.gng.springboot.account.repository.AccountRepository;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.custom.BusinessException;
import com.gng.springboot.jwt.component.JwtTokenProvider;
import com.gng.springboot.jwt.model.AccountRefreshEntity;
import com.gng.springboot.jwt.model.JwtRefreshDto;
import com.gng.springboot.jwt.repository.AccountRefreshRepository;

import lombok.RequiredArgsConstructor;

/**
 * Jwt service
 * @author gchyoo
 *
 */
@RequiredArgsConstructor
@Service
public class JwtService implements JwtServiceImpl {
	
	private final AccountRepository accountRepository;
	private final AccountRefreshRepository accountRefreshRepository;
	private final JwtTokenProvider jwtTokenProvider;

	/**
	 * Refresh token
	 * @param jwtRefreshDto 
	 * @return jwtRefreshDto with refreshed access token
	 */
	public JwtRefreshDto refreshToken(JwtRefreshDto jwtRefreshDto) {
		// Validate refresh token
		if(!jwtTokenProvider.isTokenValid(jwtRefreshDto.getRefreshToken(), JwtTokenProvider.X_AUTH_REFRESH_TOKEN)) {
			throw new BusinessException(ResponseCode.JWT_REFRESH_TOKEN_INVALID);
		}
		
		// Get user ID
		AccountEntity accountEntity = accountRepository.findById(jwtRefreshDto.getId())
				.orElseThrow(() -> new BusinessException(ResponseCode.ACCOUNT_NOT_FOUND));
		
		String uuid = jwtTokenProvider.getClaimsFromToken(jwtRefreshDto.getRefreshToken(), JwtTokenProvider.X_AUTH_REFRESH_TOKEN)
				.getBody()
				.get("uuid")
				.toString();
		
		// Select match refresh token by uuid and accountId
		AccountRefreshEntity accountRefreshEntity = accountRefreshRepository.findByGngAccountIdAndUuid(accountEntity.getGngAccountId(), uuid)
				.orElseThrow(() -> new BusinessException(ResponseCode.JWT_MISMATCH));
		
		// Check Access/Refresh token is same
		if(!accountRefreshEntity.getAccessToken().equals(jwtRefreshDto.getAccessToken())) {
			throw new BusinessException(ResponseCode.JWT_ACCESS_TOKEN_INVALID);
		}
		if(!accountRefreshEntity.getRefreshToken().equals(jwtRefreshDto.getRefreshToken())) {
			throw new BusinessException(ResponseCode.JWT_REFRESH_TOKEN_INVALID);
		}
		
		// Refresh access token
		accountRefreshEntity.setAccessToken(jwtTokenProvider.createAccessToken(accountEntity.getId(), accountEntity.getRoleTypeSet()));
		
		// Save new access token
		accountRefreshRepository.save(accountRefreshEntity);
		
		jwtRefreshDto.setAccessToken(accountRefreshEntity.getAccessToken());
		
		return jwtRefreshDto;
	}
}
