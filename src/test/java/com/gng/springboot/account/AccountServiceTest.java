package com.gng.springboot.account;


import static org.hamcrest.CoreMatchers.any;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import com.gng.springboot.account.model.AccountEntity;
import com.gng.springboot.account.model.AccountRegisterDto;
import com.gng.springboot.account.repository.AccountRepository;
import com.gng.springboot.account.service.AccountService;
import com.gng.springboot.email.service.EmailConfirmService;
import com.gng.springboot.jwt.component.JwtTokenProvider;
import com.gng.springboot.jwt.repository.AccountRefreshRepository;
import com.ulisesbocchio.jasyptspringboot.configuration.EnableEncryptablePropertiesConfiguration;

/**
 * Account service test
 * @author gchyoo
 *
 */
@TestPropertySource(locations = "classpath:application.yml")
@Import(value = {EnableEncryptablePropertiesConfiguration.class})
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Embedded DB 사용하지 않음
@DataJpaTest // JPA 테스트
@DisplayName("AccountService 테스트")
public class AccountServiceTest {
	
	@InjectMocks // Mock을 주입할 대상 객체
	private AccountService accountService;
	
	@Mock // InjectMocks에 주입할 객체
	private AccountRepository accountRepository;
	
	@Mock
	private EmailConfirmService emailConfirmService;
	
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
		Mockito.lenient() // 필요한 setUp이므로 lenient를 설정하여 예외 방지
				.when(accountRepository.save(accountEntity))
				.thenReturn(accountEntity);
		
		// When
		String id = accountService.accountRegister(accountRegisterDto);
		
		// Then
		Assertions.assertTrue(accountRegisterDto.getId().equals(id));
	}
	
	@DisplayName("AccountController 계정 인증 완료 테스트")
	@Test
	public void accountConfirmTest() throws Exception {
		// Given
		final AccountEntity accountEntity = createAccountEntity();
		final AccountRegisterDto accountRegisterDto = createAccountRegisterDto();
		Mockito.lenient() // 필요한 setUp이므로 lenient를 설정하여 예외 방지
				.when(accountRepository.save(accountEntity))
				.thenReturn(accountEntity);
		
		// When
		boolean success = accountService.accountConfirm(accountRegisterDto.getId());
		
		// Then
		Assertions.assertTrue(success);
	}
	
	public AccountRegisterDto createAccountRegisterDto() {
		return AccountRegisterDto.builder()
				.id("testId@test.com")
				.name("testName")
				.pwd("testPwd1!")
				.build();
	}
	
	public AccountEntity createAccountEntity() {
		return AccountEntity.builder()
				.id("testId@test.com")
				.name("testName")
				.pwd(passwordEncoder.encode("testPwd1!"))
				.build();
	}
}
