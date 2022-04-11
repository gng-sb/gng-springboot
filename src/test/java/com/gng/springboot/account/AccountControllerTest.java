package com.gng.springboot.account;

import java.nio.charset.StandardCharsets;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.gng.springboot.account.controller.AccountController;
import com.gng.springboot.account.model.AccountLoginDto;
import com.gng.springboot.account.model.AccountRegisterDto;
import com.gng.springboot.account.repository.AccountRepository;
import com.gng.springboot.account.service.AccountService;
import com.gng.springboot.commons.constant.Constants;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.custom.BusinessException;
import com.gng.springboot.commons.exception.handler.RestAPIExceptionHandler;
import com.gng.springboot.jwt.component.JwtTokenProvider;
import com.gng.springboot.jwt.service.JwtService;
import com.google.gson.Gson;
import com.ulisesbocchio.jasyptspringboot.configuration.EnableEncryptablePropertiesConfiguration;

/**
 * Account controller test
 * @author gchyoo
 *
 */
@TestPropertySource(locations = "classpath:application.yml")
@Import(value = {EnableEncryptablePropertiesConfiguration.class})
@ExtendWith(SpringExtension.class)
@MockBean(classes = {JwtTokenProvider.class, JwtService.class, AccountRepository.class})
@AutoConfigureMockMvc
@WebMvcTest(AccountController.class) // 테스트할 클래스명
@DisplayName("AccountController 테스트")
public class AccountControllerTest {
	private MockMvc mockMvc;
	
	@MockBean
	private AccountService accountService;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new AccountController(accountService))
				.setControllerAdvice(RestAPIExceptionHandler.class) // ControllerAdvice 클래스 추가
				.addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.toString(), true)) // UTF-8 필터 추가
				.build();
	}
	
	@Nested
	@DisplayName("AccountController 계정 등록 테스트")
	public class AccountRegisterTest {
		private MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/account/register")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8.toString());
		
		@Test
		@DisplayName("성공")
		public void success() throws Exception {
			// Given
			final AccountRegisterDto accountRegisterDto = AccountRegisterDto.builder()
					.accountId("testId@test.com")
					.accountName("testName")
					.accountPwd("testPwd1!")
					.build();
					
			BDDMockito.given(accountService.accountRegister(accountRegisterDto))
					.willReturn(accountRegisterDto.getAccountId());
			
			// When
			final ResultActions resultActions = mockMvc.perform(request.content(new Gson().toJson(accountRegisterDto)));
			
			// Then
			resultActions
					.andDo(MockMvcResultHandlers.print()) // 응답 출력
					.andExpect(MockMvcResultMatchers.status().isCreated())
					.andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(ResponseCode.ACCOUNT_REGISTER_SUCCESS.getHttpStatus().name()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.messages[0]").value(ResponseCode.ACCOUNT_REGISTER_SUCCESS.getMessage()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
		}

		@Test
		@DisplayName("실패")
		public void fail() throws Exception {
			// Given
			final AccountRegisterDto accountRegisterDto = AccountRegisterDto.builder()
					.accountId("")
					.accountName("")
					.accountPwd("failpwd")
					.build();
					
			BDDMockito.given(accountService.accountRegister(accountRegisterDto))
					.willThrow(BusinessException.class);
			
			// When
			final ResultActions resultActions = mockMvc.perform(request.content(new Gson().toJson(accountRegisterDto)));
			
			// Then
			resultActions
					.andDo(MockMvcResultHandlers.print()) // 응답 출력
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(ResponseCode.BAD_REQUEST.getHttpStatus().name()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.messages").value(Matchers.containsInAnyOrder( // 순서 상관없이 포함하고 있는지 확인
							Constants.VALIDATE_ACCOUNT_PW_PATTERN, Constants.VALIDATE_ACCOUNT_ID_EMAIL, Constants.VALIDATE_ACCOUNT_NAME_SIZE
							)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
		}
	}
	

	@Nested
	@DisplayName("AccountController 계정 로그인 테스트")
	public class AccountLoginTest {
		private MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/account/login")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8.toString());
		
		@Test
		@DisplayName("성공")
		public void success() throws Exception {
			// Given
			final AccountLoginDto accountLoginDto = AccountLoginDto.builder()
					.accountId("testId@test.com")
					.accountPwd("testPwd1!")
					.build();
			
					
			BDDMockito.given(accountService.accountLogin(accountLoginDto))
					.willReturn(accountLoginDto);
			
			// When
			final ResultActions resultActions = mockMvc.perform(request.content(new Gson().toJson(accountLoginDto)));
			
			// Then
			resultActions
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(ResponseCode.ACCOUNT_LOGIN_SUCCESS.getHttpStatus().name()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.messages[0]").value(ResponseCode.ACCOUNT_LOGIN_SUCCESS.getMessage()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
		}

		@Test
		@DisplayName("실패")
		public void fail() throws Exception {
			// Given
			final AccountLoginDto accountLoginDto = AccountLoginDto.builder()
					.accountId("")
					.accountPwd("")
					.build();
					
			BDDMockito.given(accountService.accountLogin(accountLoginDto))
					.willThrow(BusinessException.class);
			
			// When
			final ResultActions resultActions = mockMvc.perform(request.content(new Gson().toJson(accountLoginDto)));
			
			// Then
			resultActions
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(ResponseCode.BAD_REQUEST.getHttpStatus().name()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.messages").value(Matchers.containsInAnyOrder(
							Constants.VALIDATE_ACCOUNT_ID_EMAIL, Constants.VALIDATE_ACCOUNT_PW_BLANK
							)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
		}
	}
}
