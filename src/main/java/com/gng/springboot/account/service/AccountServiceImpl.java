package com.gng.springboot.account.service;

import java.util.Optional;
import java.util.UUID;

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
import com.gng.springboot.jwt.model.AccountRefreshEntity;
import com.gng.springboot.jwt.repository.AccountRefreshRepository;

import lombok.RequiredArgsConstructor;

/**
 * Account service
 * @author gchyoo
 *
 */
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
	
	private final EmailConfirmService emailConfirmServiceImpl;
	private final AccountRepository accountRepository;
	private final AccountRefreshRepository accountRefreshRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	
	/**
	 * Register account
	 * @param accountRegisterDto
	 * @return id
	 */
	@Transactional(rollbackFor = BusinessException.class, noRollbackFor = NoRollbackBusinessException.class)
	public String accountRegister(AccountRegisterDto accountRegisterDto) {
		AccountEntity accountEntity = AccountEntity.builder()
				.id(accountRegisterDto.getId())
				.pwd(passwordEncoder.encode(accountRegisterDto.getPwd()))
				.name(accountRegisterDto.getName())
				.status(AccountStatusTypes.NOT_AUTHORIZED.getStatus())
				.build();
		
		// Add role type(ROLE_USER)
		accountEntity.addRoleType(RoleTypes.ROLE_USER);
		
		// Get account data with id and check conflict
		
		
		// Insert registration data if not exist
		if(!checkAccountExist(accountEntity.getId(), accountRegisterDto.getPwd())) {
			accountRepository.save(accountEntity);
		}
		
		return accountRegisterDto.getId();
	}
	
	/**
	 * Check id/pwd is same if conflict
	 * @param currentPwd
	 * @return
	 */
	public boolean checkAccountExist(String id, String currentPwd) {
		Optional<AccountEntity> prevAccountEntityOptional = accountRepository.findById(id);
		
		// Account conflict exceptions
		if(prevAccountEntityOptional != null) {
			prevAccountEntityOptional.ifPresent(account -> {
				if(account.getAccountStatus().equals(AccountStatusTypes.USE)) {
					// Do not send confirmation mail if account is already authorized
					throw new BusinessException(ResponseCode.ACCOUNT_REGISTER_ID_CONFLICT);
				} else if(!passwordEncoder.matches(currentPwd, account.getPwd())) {
					// Do not send confirmation mail if account is not authorized and password is different
					throw new BusinessException(ResponseCode.ACCOUNT_REGISTER_PASSWORD_FAILURE);
				}
			});
		}
		
		return false;
	}
	
	/**
	 * Confirm account
	 * @param id
	 */
	@Transactional(rollbackFor = BusinessException.class, noRollbackFor = NoRollbackBusinessException.class)
	public boolean accountConfirm(String id) {
		// Retrieve account data
		AccountEntity accountEntity = accountRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.ACCOUNT_NOT_FOUND));
		
		// Set account status to 'USE'
		accountEntity.setAccountStatus(AccountStatusTypes.USE);
		
		// Update account status
		accountRepository.save(accountEntity);
		
		return true;
	}
	
	/**
	 * Login account with account id/password
	 * @param accountLoginDto
	 * @return accountLoginDto with JWT
	 */
	@Transactional(rollbackFor = BusinessException.class, noRollbackFor = NoRollbackBusinessException.class)
	public AccountLoginDto accountLogin(AccountLoginDto accountLoginDto) {
		// Get account data with id
		AccountEntity accountEntity = accountRepository.findById(accountLoginDto.getId())
				.orElseThrow(() -> new BusinessException(ResponseCode.ACCOUNT_NOT_EXIST));
		
		// Check password
		if(!passwordEncoder.matches(accountLoginDto.getPwd(), accountEntity.getPwd())) {
			throw new BusinessException(ResponseCode.ACCOUNT_LOGIN_PASSWORD_FAILURE);
		}
		
		// Check user status
		if(accountEntity.getAccountStatus().equals(AccountStatusTypes.NOT_AUTHORIZED)) {
			emailConfirmServiceImpl.sendEmailConfirmToken(accountEntity.getId());
			
			throw new BusinessException(ResponseCode.ACCOUNT_LOGIN_NOT_AUTHORIZED);
		} else if(accountEntity.getAccountStatus().equals(AccountStatusTypes.BLOCKED)) {
			throw new BusinessException(ResponseCode.ACCOUNT_LOGIN_BLOCKED);
		}
		
		// UUID for refresh token's key
		String uuid = UUID.randomUUID().toString();
		String accessToken = jwtTokenProvider.createAccessToken(accountEntity.getId(), accountEntity.getRoleTypeSet());
		String refreshToken = jwtTokenProvider.createRefreshToken(uuid);
		
		// Build refresh token data
		AccountRefreshEntity accountRefreshEntity = AccountRefreshEntity.builder()
				.gngAccountId(accountEntity.getGngAccountId())
				.uuid(uuid)
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();

		// Insert refresh token data
		accountRefreshRepository.save(accountRefreshEntity);
		
		return AccountLoginDto.of(accountEntity, accessToken, refreshToken);
	}
}
