package com.gng.springboot.account;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.gng.springboot.account.controller.AccountController;
import com.gng.springboot.account.model.AccountEntity;
import com.gng.springboot.account.model.AccountLoginDto;
import com.gng.springboot.account.model.AccountRegisterDto;
import com.gng.springboot.account.repository.AccountRepository;
import com.gng.springboot.account.service.AccountService;
import com.gng.springboot.commons.base.BaseControllerTest;
import com.gng.springboot.commons.constant.Constants;
import com.gng.springboot.commons.constant.Constants.AccountStatusTypes;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.handler.RestAPIExceptionHandler;
import com.gng.springboot.email.model.EmailConfirmEntity;
import com.gng.springboot.email.repository.EmailConfirmRepository;
import com.gng.springboot.email.service.EmailConfirmService;

/**
 * Account controller test
 * @author gchyoo
 *
 */
@DisplayName("AccountController 테스트")
public class AccountControllerTest extends BaseControllerTest {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountRepository accountRepository;
	
	// Prevent email confirm in test
	@MockBean
	private EmailConfirmService emailConfirmServiceImpl;
	
	@Autowired
	private EmailConfirmRepository emailConfirmRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final static String id = "testId@test.com";
	private final static String name = "testName";
	private final static String pwd = "testPwd1!";
	
	@BeforeEach
	protected void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new AccountController(accountService, emailConfirmServiceImpl))
				.setControllerAdvice(RestAPIExceptionHandler.class) // ControllerAdvice 클래스 추가
				.addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.toString(), true)) // UTF-8 필터 추가
				.build();
	}
	
	@Nested
	@DisplayName("AccountController 계정 등록 테스트")
	public class AccountRegisterTest {
		private String uri = "/account/register";
		
		@Test
		@DisplayName("성공")
		public void success() throws Exception {
			// Given
			final AccountRegisterDto accountRegisterDto = AccountRegisterDto.builder()
					.id(id)
					.name(name)
					.pwd(pwd)
					.build();
			
			EmailConfirmEntity emailConfirmEntity = EmailConfirmEntity.createEmailConfirmToken(id, 5000L);
			emailConfirmRepository.save(emailConfirmEntity);
			
			// When
			final ResultActions resultActions = mockMvc.perform(getRequest(uri).content(toJson(accountRegisterDto)));
			
			// Then
			resultActions
					.andDo(print()) // 응답 출력
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.httpStatus").value(ResponseCode.ACCOUNT_REGISTER_SUCCESS.getHttpStatus().name()))
					.andExpect(jsonPath("$.messages[0]").value(ResponseCode.ACCOUNT_REGISTER_SUCCESS.getMessage()))
					.andExpect(jsonPath("$.result").value(id))
					.andExpect(jsonPath("$.success").value(true));
		}

		@Test
		@DisplayName("중복된 아이디")
		public void conflict() throws Exception {
			// Given
			final AccountRegisterDto accountRegisterDto = AccountRegisterDto.builder()
					.id(id)
					.name(name)
					.pwd(pwd)
					.build();
			AccountEntity accountEntity = modelMapper.map(accountRegisterDto, AccountEntity.class);
			accountEntity.setPwd(passwordEncoder.encode(pwd));
			accountEntity.setStatus(AccountStatusTypes.USE);
			
			accountRepository.save(accountEntity);
			
			// When
			final ResultActions resultActions = mockMvc.perform(getRequest(uri).content(toJson(accountRegisterDto)));
			
			// Then
			resultActions
					.andDo(print()) // 응답 출력
					.andExpect(status().isConflict())
					.andExpect(jsonPath("$.httpStatus").value(ResponseCode.ACCOUNT_REGISTER_ID_CONFLICT.getHttpStatus().name()))
					.andExpect(jsonPath("$.messages").value(Matchers.containsInAnyOrder(
							ResponseCode.ACCOUNT_REGISTER_ID_CONFLICT.getMessage()
							)))
					.andExpect(jsonPath("$.success").value(false));
		}
		
		@Test
		@DisplayName("잘못된 입력값")
		public void badRequest() throws Exception {
			// Given
			final AccountRegisterDto accountRegisterDto = AccountRegisterDto.builder()
					.id("")
					.name("")
					.pwd("failpwd")
					.build();
					
			// When
			final ResultActions resultActions = mockMvc.perform(getRequest(uri).content(toJson(accountRegisterDto)));
			
			// Then
			resultActions
					.andDo(print()) // 응답 출력
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.httpStatus").value(ResponseCode.BAD_REQUEST.getHttpStatus().name()))
					.andExpect(jsonPath("$.messages").value(Matchers.containsInAnyOrder( // 순서 상관없이 포함하고 있는지 확인
							Constants.VALIDATE_ACCOUNT_PW_PATTERN, Constants.VALIDATE_ACCOUNT_ID_EMAIL, Constants.VALIDATE_ACCOUNT_NAME_SIZE
							)))
					.andExpect(jsonPath("$.success").value(false));
		}
	}
	

	@Nested
	@DisplayName("AccountController 계정 로그인 테스트")
	public class AccountLoginTest {
		
		private String uri = "/account/login";

		@Test
		@DisplayName("성공")
		public void success() throws Exception {
			// Given
			final AccountLoginDto accountLoginDto = AccountLoginDto.builder()
					.id(id)
					.pwd(pwd)
					.build();
			
			final AccountEntity accountEntity = modelMapper.map(accountLoginDto, AccountEntity.class);
			accountEntity.setGngAccountId(1L);
			accountEntity.setStatus(AccountStatusTypes.USE);
			accountEntity.setPwd(passwordEncoder.encode(pwd));
			
			accountRepository.save(accountEntity);
			
			// When
			final ResultActions resultActions = mockMvc.perform(getRequest(uri).content(toJson(accountLoginDto)));
			
			// Then
			resultActions
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.httpStatus").value(ResponseCode.ACCOUNT_LOGIN_SUCCESS.getHttpStatus().name()))
					.andExpect(jsonPath("$.messages[0]").value(ResponseCode.ACCOUNT_LOGIN_SUCCESS.getMessage()))
					.andExpect(jsonPath("$.result.id").value(id))
					.andExpect(jsonPath("$.success").value(true));
		}

		@Test
		@DisplayName("이메일 인증되지 않은 계정")
		public void unauthorized() throws Exception {
			// Given
			final AccountLoginDto accountLoginDto = AccountLoginDto.builder()
					.id(id)
					.pwd(pwd)
					.build();
			
			final AccountEntity accountEntity = modelMapper.map(accountLoginDto, AccountEntity.class);
			accountEntity.setGngAccountId(1L);
			accountEntity.setPwd(passwordEncoder.encode(pwd));
			
			accountRepository.save(accountEntity);
			
			// When
			final ResultActions resultActions = mockMvc.perform(getRequest(uri).content(toJson(accountLoginDto)));
			
			// Then
			resultActions
					.andDo(print())
					.andExpect(status().isUnauthorized())
					.andExpect(jsonPath("$.httpStatus").value(ResponseCode.ACCOUNT_LOGIN_NOT_AUTHORIZED.getHttpStatus().name()))
					.andExpect(jsonPath("$.messages[0]").value(ResponseCode.ACCOUNT_LOGIN_NOT_AUTHORIZED.getMessage()))
					.andExpect(jsonPath("$.success").value(false));
		}
		
		@Test
		@DisplayName("잘못된 입력값")
		public void badRequest() throws Exception {
			// Given
			final AccountLoginDto accountLoginDto = AccountLoginDto.builder()
					.id("")
					.pwd("")
					.build();
					
			// When
			final ResultActions resultActions = mockMvc.perform(getRequest(uri).content(toJson(accountLoginDto)));
			
			// Then
			resultActions
					.andDo(print())
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.httpStatus").value(ResponseCode.BAD_REQUEST.getHttpStatus().name()))
					.andExpect(jsonPath("$.messages").value(Matchers.containsInAnyOrder(
							Constants.VALIDATE_ACCOUNT_ID_EMAIL, Constants.VALIDATE_ACCOUNT_PW_BLANK
							)))
					.andExpect(jsonPath("$.success").value(false));
		}
	}
}
