package com.gng.springboot.account.service;

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
import com.gng.springboot.commons.util.PasswordEncryptionUtil;
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
	private final PasswordEncryptionUtil passwordEncryptionUtil;
	private final JwtTokenProvider jwtTokenProvider;
	
	/**
	 * Register account
	 * @param accountRegisterDto
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public String accountRegister(AccountRegisterDto accountRegisterDto) {
		AccountEntity accountEntity = AccountEntity.builder()
				.accountId(accountRegisterDto.getAccountId())
				.accountPwd(passwordEncryptionUtil.encrypt(accountRegisterDto.getAccountPwd()))
				.accountName(accountRegisterDto.getAccountName())
				.accountStatus(AccountStatusTypes.NOT_AUTHORIZED.getStatus())
				.build();
		
		// Add role type(ROLE_USER)
		accountEntity.addRoleType(RoleTypes.ROLE_USER);
		
		// Get account data with accountId and check conflict
		accountRepository.findByAccountId(accountEntity.getAccountId()).ifPresent(account -> {
			throw new BusinessException(ResponseCode.ACCOUNT_ID_CONFLICT);
		});
		
		// Insert registeration data
		accountRepository.save(accountEntity).getAccountId();
		
		// Send confirmation mail
		emailConfirmService.sendEmailConfirmToken(accountEntity.getAccountId());
		
		return accountEntity.getAccountId();
	}
	
	/**
	 * Confirm account
	 * @param accountId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void confirmAccount(String accountId) {
		// Retrieve account data
		AccountEntity accountEntity = accountRepository.findByAccountId(accountId)
				.orElseThrow(() -> new BusinessException(ResponseCode.ACCOUNT_NOT_FOUND));

		accountEntity.setAccountStatus(AccountStatusTypes.USE);

		accountRepository.save(accountEntity);
	}
	
	/**
	 * Login account with account id/password
	 * @param accountLoginDto
	 * @return
	 */
	public AccountLoginDto accountLogin(AccountLoginDto accountLoginDto) {
		// Get account data with accountId
		AccountEntity accountEntity = accountRepository.findByAccountId(accountLoginDto.getAccountId())
				.orElseThrow(() -> new BusinessException(ResponseCode.ACCOUNT_NOT_EXIST));
		
		// Check password
		if(!passwordEncryptionUtil.isValidAccountPwd(accountEntity.getAccountPwd(), accountLoginDto.getAccountPwd())) {
			throw new BusinessException(ResponseCode.ACCOUNT_LOGIN_PASSWORD_FAILURE);
		}
		
		// Check user status
		if(accountEntity.getAccountStatus().equals(AccountStatusTypes.NOT_AUTHORIZED)) {
			throw new BusinessException(ResponseCode.ACCOUNT_LOGIN_NOT_AUTHORIZED);
		} else if(accountEntity.getAccountStatus().equals(AccountStatusTypes.BLOCKED)) {
			throw new BusinessException(ResponseCode.ACCOUNT_LOGIN_BLOCKED);
		}
		
		return AccountLoginDto.of(accountEntity, jwtTokenProvider.createToken(accountEntity.getAccountId(), accountEntity.getRoleTypeSet()));
	}
}
