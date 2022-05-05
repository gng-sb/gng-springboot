package com.gng.springboot.account;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gng.springboot.account.model.AccountEntity;
import com.gng.springboot.account.model.AccountRegisterDto;
import com.gng.springboot.account.repository.AccountRepository;
import com.gng.springboot.account.service.AccountServiceImpl;
import com.gng.springboot.commons.base.BaseServiceTest;
import com.gng.springboot.jwt.component.JwtTokenProvider;
import com.gng.springboot.jwt.repository.AccountRefreshRepository;

/**
 * Account service test
 * @author gchyoo
 *
 */
@DisplayName("AccountService 테스트")
public class AccountServiceTest extends BaseServiceTest {
	
	@InjectMocks // Mock을 주입할 객체
	private AccountServiceImpl accountServiceImpl;
	
	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private AccountRefreshRepository accountRefreshRepository;
	
	@Mock
	private JwtTokenProvider jwtTokenProvider;
	
	@DisplayName("AccountController 계정 등록 테스트")
	@Test
	public void accountRegisterTest() throws Exception {
		// Given
		final AccountEntity accountEntity = createAccountEntity();
		final AccountRegisterDto accountRegisterDto = createAccountRegisterDto();

		BDDMockito.given(accountRepository.save(accountEntity))
				.willReturn(accountEntity);
		
		// When
		String id = accountServiceImpl.accountRegister(accountRegisterDto);
		
		// Then
		assertThat(id).isEqualTo(accountRegisterDto.getId());
	}
	
	@DisplayName("AccountController 계정 인증 완료 테스트")
	@Test
	public void accountConfirmTest() throws Exception {
		// Given
		final AccountEntity accountEntity = createAccountEntity();
		final AccountRegisterDto accountRegisterDto = createAccountRegisterDto();

		
		BDDMockito.given(accountRepository.save(accountEntity))
				.willReturn(accountEntity);
		BDDMockito.given(accountRepository.findById(accountEntity.getId()))
				.willReturn(Optional.of(accountEntity));
		
		// When
		boolean success = accountServiceImpl.accountConfirm(accountRegisterDto.getId());
		
		// Then
		assertThat(success).isTrue();
	}
	
	public AccountRegisterDto createAccountRegisterDto() {
		return AccountRegisterDto.builder()
				.id("testId@test.com")
				.name("testName")
				.pwd("testPwd1!")
				.build();
	}
	
	public AccountEntity createAccountEntity() {
		AccountEntity accountEntity = AccountEntity.builder()
				.id("testId@test.com")
				.name("testName")
				.pwd(passwordEncoder.encode("testPwd1!"))
				.build();
		
		return accountEntity;
	}
}
