package com.gng.springboot.account.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gng.springboot.account.model.AccountEntity;
import com.gng.springboot.account.model.AccountLoginDto;
import com.gng.springboot.account.model.AccountRegisterDto;
import com.gng.springboot.account.repository.AccountRepository;
import com.gng.springboot.commons.constant.Constants.AccountStatusTypes;
import com.gng.springboot.commons.constant.Constants.RoleTypes;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.custom.BusinessException;
import com.gng.springboot.commons.exception.custom.NoRollbackBusinessException;
import com.gng.springboot.email.service.EmailConfirmService;
import com.gng.springboot.jwt.component.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

/**
 * Account service
 * @author gchyoo
 *
 */
@RequiredArgsConstructor
@Service
public class AccountService {
	
	private final AccountRepository accountRepository;
	private final EmailConfirmService emailConfirmService;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	
	/**
	 * Register account
	 * @param accountRegisterDto
	 * @return accountId
	 */
	@Transactional(rollbackFor = BusinessException.class, noRollbackFor = NoRollbackBusinessException.class)
	public String accountRegister(AccountRegisterDto accountRegisterDto) {
		AccountEntity accountEntity = AccountEntity.builder()
				.accountId(accountRegisterDto.getAccountId())
				.accountPwd(passwordEncoder.encode(accountRegisterDto.getAccountPwd()))
				.accountName(accountRegisterDto.getAccountName())
				.accountStatus(AccountStatusTypes.NOT_AUTHORIZED.getStatus())
				.build();
		
		// Add role type(ROLE_USER)
		accountEntity.addRoleType(RoleTypes.ROLE_USER);
		
		// Get account data with accountId and check conflict
		Optional<AccountEntity> prevAccountEntityOptional = accountRepository.findByAccountId(accountEntity.getAccountId());
		
		// Account conflict exceptions
		prevAccountEntityOptional.ifPresent(account -> {
			if(account.getAccountStatus().equals(AccountStatusTypes.USE)) {
				// Do not send confirmation mail if account is already authorized
				throw new BusinessException(ResponseCode.ACCOUNT_REGISTER_ID_CONFLICT);
			} else if(!passwordEncoder.matches(accountEntity.getAccountPwd(), accountRegisterDto.getAccountPwd())) {
				// Do not send confirmation mail if account is not authorized and password is different
				throw new BusinessException(ResponseCode.ACCOUNT_REGISTER_PASSWORD_FAILURE);
			}
		});
		
		// Insert registration data if not exist
		if(!prevAccountEntityOptional.isPresent()) {
			accountRepository.save(accountEntity).getAccountId();
		}
		
		// Send confirmation mail
		emailConfirmService.sendEmailConfirmToken(accountEntity.getAccountId());
		
		return accountEntity.getAccountId();
	}
	
	/**
	 * Confirm account
	 * @param accountId
	 */
	@Transactional(rollbackFor = BusinessException.class, noRollbackFor = NoRollbackBusinessException.class)
	public void confirmAccount(String accountId) {
		// Retrieve account data
		AccountEntity accountEntity = accountRepository.findByAccountId(accountId)
				.orElseThrow(() -> new BusinessException(ResponseCode.ACCOUNT_NOT_FOUND));
		
		// Set account status to 'USE'
		accountEntity.setAccountStatus(AccountStatusTypes.USE);
		
		// Update account status
		accountRepository.save(accountEntity);
	}
	
	/**
	 * Login account with account id/password
	 * @param accountLoginDto
	 * @return accountLoginDto with JWT
	 */
	public AccountLoginDto accountLogin(AccountLoginDto accountLoginDto) {
		// Get account data with accountId
		AccountEntity accountEntity = accountRepository.findByAccountId(accountLoginDto.getAccountId())
				.orElseThrow(() -> new BusinessException(ResponseCode.ACCOUNT_NOT_EXIST));
		
		// Check password
		if(!passwordEncoder.matches(accountEntity.getAccountPwd(), accountLoginDto.getAccountPwd())) {
			throw new BusinessException(ResponseCode.ACCOUNT_LOGIN_PASSWORD_FAILURE);
		}
		
		// Check user status
		if(accountEntity.getAccountStatus().equals(AccountStatusTypes.NOT_AUTHORIZED)) {
			// Re-send confirmation mail if unauthorized account trying to login
			emailConfirmService.sendEmailConfirmToken(accountEntity.getAccountId());
			
			throw new BusinessException(ResponseCode.ACCOUNT_LOGIN_NOT_AUTHORIZED);
		} else if(accountEntity.getAccountStatus().equals(AccountStatusTypes.BLOCKED)) {
			throw new BusinessException(ResponseCode.ACCOUNT_LOGIN_BLOCKED);
		}
		
		return AccountLoginDto.of(accountEntity, jwtTokenProvider.createToken(accountEntity.getAccountId(), accountEntity.getRoleTypeSet()));
	}
}
