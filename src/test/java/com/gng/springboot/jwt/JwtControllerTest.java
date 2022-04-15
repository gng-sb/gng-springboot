package com.gng.springboot.jwt;

import java.nio.charset.StandardCharsets;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.gng.springboot.account.controller.AccountController;
import com.gng.springboot.account.model.AccountRegisterDto;
import com.gng.springboot.account.service.AccountService;
import com.gng.springboot.commons.constant.Constants;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.custom.BusinessException;
import com.gng.springboot.commons.exception.handler.RestAPIExceptionHandler;
import com.gng.springboot.jwt.component.JwtTokenProvider;
import com.gng.springboot.jwt.controller.JwtController;
import com.gng.springboot.jwt.model.JwtRefreshDto;
import com.gng.springboot.jwt.repository.AccountRefreshRepository;
import com.gng.springboot.jwt.service.JwtService;
import com.google.gson.Gson;
import com.ulisesbocchio.jasyptspringboot.configuration.EnableEncryptablePropertiesConfiguration;

/**
 * JWT controller test
 * @author gchyoo
 *
 */
@TestPropertySource(locations = "classpath:application.yml")
@Import(value = {EnableEncryptablePropertiesConfiguration.class})
@WebMvcTest(JwtController.class) // 테스트할 컨트롤러 클래스명
@MockBean(classes = {
		JwtTokenProvider.class, JwtService.class,
		AccountService.class, AccountRefreshRepository.class})
@ExtendWith(MockitoExtension.class)
@DisplayName("JwtController 테스트")
public class JwtControllerTest {
	private MockMvc mockMvc;
	
	@MockBean
	private JwtService jwtService;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new JwtController(jwtService))
				.setControllerAdvice(RestAPIExceptionHandler.class) // ControllerAdvice 클래스 추가
				.addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.toString(), true)) // UTF-8 필터 추가
				.build();
	}
	
	@Nested
	@DisplayName("JwtController JWT 재발급 테스트")
	public class AccountRegisterTest {
		private MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/account/register")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8.toString());
		
		@Test
		@DisplayName("성공")
		public void success() throws Exception {
			// Given
			final JwtRefreshDto jwtRefreshDto = JwtRefreshDto.builder()
					.id("testId@test.com")
					.accessToken("testName")
					.refreshToken("testPwd1!")
					.build();
					
			BDDMockito.given(jwtService.refreshToken(jwtRefreshDto))
					.willReturn(jwtRefreshDto);
			
			// When
			final ResultActions resultActions = mockMvc.perform(request.content(new Gson().toJson(jwtRefreshDto)));
			
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
			final JwtRefreshDto jwtRefreshDto = JwtRefreshDto.builder()
					.id("")
					.accessToken("")
					.refreshToken("")
					.build();
					
			BDDMockito.given(jwtService.refreshToken(jwtRefreshDto))
					.willThrow(BusinessException.class);
			
			// When
			final ResultActions resultActions = mockMvc.perform(request.content(new Gson().toJson(jwtRefreshDto)));
			
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
}
