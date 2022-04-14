package com.gng.springboot.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.gng.springboot.account.repository.AccountRepository;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.custom.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Jwt token provider service
 * @author gchyoo
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class JwtTokenProviderService implements UserDetailsService {
	
	private final AccountRepository accountRepository;

	/**
	 * Load account by user name(id)
	 */
	@Override
	public UserDetails loadUserByUsername(String id) {
		log.debug("Load account by [id={}]", id);
		
		return accountRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.ACCOUNT_NOT_FOUND));
	}
}
