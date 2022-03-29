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
 * Jwt service
 * @author gchyoo
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService implements UserDetailsService {
	
	private final AccountRepository accountRepository;

	/**
	 * Load account by accountname(accountId)
	 */
	@Override
	public UserDetails loadUserByUsername(String accountId) {
		log.debug("Load account by [accountId={}]", accountId);
		
		return accountRepository.findByAccountId(accountId)
				.orElseThrow(() -> new BusinessException(ResponseCode.ACCOUNT_NOT_FOUND));
	}
}
