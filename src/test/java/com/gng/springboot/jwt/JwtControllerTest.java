package com.gng.springboot.jwt;

import java.nio.charset.StandardCharsets;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.gng.springboot.commons.base.BaseControllerTest;
import com.gng.springboot.commons.constant.Constants;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.custom.BusinessException;
import com.gng.springboot.commons.exception.handler.RestAPIExceptionHandler;
import com.gng.springboot.jwt.controller.JwtController;
import com.gng.springboot.jwt.model.JwtRefreshDto;
import com.gng.springboot.jwt.service.JwtService;
import com.google.gson.Gson;

/**
 * JWT controller test
 * @author gchyoo
 *
 */
@DisplayName("JwtController 테스트")
public class JwtControllerTest extends BaseControllerTest {
	
	@MockBean
	private JwtService jwtServiceImpl;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new JwtController(jwtServiceImpl))
				.setControllerAdvice(RestAPIExceptionHandler.class) // ControllerAdvice 클래스 추가
				.addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.toString(), true)) // UTF-8 필터 추가
				.build();
	}
	
	@Nested
	@DisplayName("JwtController JWT 재발급 테스트")
	public class AccountRegisterTest {
		private MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/jwt")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8.toString());
		
		@Test
		@DisplayName("성공")
		public void success() throws Exception {
			// Given
			final JwtRefreshDto jwtRefreshDto = JwtRefreshDto.builder()
					.id("testId@test.com")
					.accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhMDEwOTI2MDM5MDlAZ21haWwuY29tIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY0OTkyODcyMywiZXhwIjoxNjQ5OTI5MDIzfQ.PUyM6zDAS2lvptw_tNBbagtN_ORPkrkNidLt3c2imzo")
					.refreshToken("eyJhbGciOiJIUzI1NiJ9.eyJ1dWlkIjoiYmU0NDUzZjAtZjJiNy00M2JlLThlZDItMzg5ZDc4OGIzMTE4IiwiaWF0IjoxNjQ5OTI4NzIzLCJleHAiOjE2NTA1MzM1MjN9.WYFqPcV_vQklZgGen3VF3Y6Wlt6B6-on9sA5nMeGSag")
					.build();
					
			BDDMockito.given(jwtServiceImpl.refreshToken(jwtRefreshDto))
					.willReturn(jwtRefreshDto);
			
			// When
			final ResultActions resultActions = mockMvc.perform(request.content(new Gson().toJson(jwtRefreshDto)));
			
			// Then
			resultActions
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(ResponseCode.JWT_REFRESH_SUCCESS.getHttpStatus().name()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.messages[0]").value(ResponseCode.JWT_REFRESH_SUCCESS.getMessage()))
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
					
			BDDMockito.given(jwtServiceImpl.refreshToken(jwtRefreshDto))
					.willThrow(BusinessException.class);
			
			// When
			final ResultActions resultActions = mockMvc.perform(request.content(new Gson().toJson(jwtRefreshDto)));
			
			// Then
			resultActions
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(ResponseCode.BAD_REQUEST.getHttpStatus().name()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.messages").value(Matchers.containsInAnyOrder(
							Constants.JWT_ACCESS_TOKEN_EMPTY, Constants.JWT_REFRESH_TOKEN_EMPTY, Constants.VALIDATE_ACCOUNT_ID_BLANK
							)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
		}
	}
}
