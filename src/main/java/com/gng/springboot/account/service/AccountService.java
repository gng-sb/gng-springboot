package com.gng.springboot.account.service;

import org.springframework.stereotype.Service;

import com.gng.springboot.commons.constant.Constants.RoleTypes;
import com.gng.springboot.commons.constant.Constants.AccountStatusTypes;
import com.gng.springboot.account.model.AccountEntity;
import com.gng.springboot.account.model.AccountLoginDto;
import com.gng.springboot.account.model.AccountRegisterDto;
import com.gng.springboot.account.repository.AccountRepository;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.custom.BusinessException;
import com.gng.springboot.commons.util.PasswordEncryptionUtil;
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
	private final PasswordEncryptionUtil passwordEncryptionUtil;
	private final JwtTokenProvider jwtTokenProvider;
	
	/**
	 * Register account
	 * @param accountRegisterDto
	 * @return
	 */
	public String registerAccount(AccountRegisterDto accountRegisterDto) {
		AccountEntity accountEntity = AccountEntity.builder()
				.accountId(accountRegisterDto.getAccountId())
				.accountPwd(passwordEncryptionUtil.encrypt(accountRegisterDto.getAccountPwd()))
				.accountName(accountRegisterDto.getAccountName())
				.accountStatus(AccountStatusTypes.NOT_USE.getStatus())
				.build();
		
		// Add role type(ROLE_USER)
		accountEntity.addRoleType(RoleTypes.ROLE_USER);
		
		// Get account data with accountId and check conflict
		accountRepository.findByAccountId(accountEntity.getAccountId()).ifPresent(account -> {
			throw new BusinessException(ResponseCode.ACCOUNT_ID_CONFLICT);
		});
		
		// Insert registeration data
		String id = accountRepository.save(accountEntity).getAccountId();
		
		return id;
	}
	
	/**
	 * Login account with account id/password
	 * @param accountLoginDto
	 * @return
	 */
	public AccountLoginDto loginAccount(AccountLoginDto accountLoginDto) {
		// Get account data with accountId
		AccountEntity accountEntity = accountRepository.findByAccountId(accountLoginDto.getAccountId())
				.orElseThrow(() -> new BusinessException(ResponseCode.ACCOUNT_NOT_EXIST));
		
		if(!passwordEncryptionUtil.isValidAccountPwd(accountEntity.getAccountPwd(), accountLoginDto.getAccountPwd())) {
			throw new BusinessException(ResponseCode.ACCOUNT_LOGIN_PASSWORD_FAILURE);
		}
		
		// TODO: JWT create 무한루프 수정
		return AccountLoginDto.of(accountEntity, jwtTokenProvider.createToken(accountEntity.getAccountId(), accountEntity.getRoleTypeSet()));
	}
}
