package com.gng.springboot.commons.base;

import java.nio.charset.StandardCharsets;

import org.junit.Ignore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Ignore // 테스트를 가지고 있는 클래스가 아니므로 무시
@TestPropertySource(locations = "classpath:/application-test.yml") // 테스트 프로퍼티 파일 지정
@AutoConfigureMockMvc(addFilters = false) // Spring security filter 비활성화
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test") // 테스트용 프로필로 설정
@SpringBootTest // API 테스트 시에는 SpringBootTest 사용
public class BaseControllerTest {
	
	// 요청을 위한 MockMvc 객체
	// DispatcherServlet을 포함하고, SprinbootTest보다 속도가 빠르지만 단위테스트보다 느림
	@Autowired
	protected MockMvc mockMvc;
	
	// JSON string 출력을 위한 ObjectMapper 의존성 주입
	@Autowired
	protected ObjectMapper objectMapper;
	
	// 객체 복사를 위한 modelMapper
	@Autowired
	protected ModelMapper modelMapper;
	
	private static Gson gson = new Gson();
	
	protected MockHttpServletRequestBuilder getRequest(String uri) {
		return MockMvcRequestBuilders.post(uri)
				.accept(MediaTypes.HAL_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8);
	}
	
	protected String toJson(Object object) {
		return gson.toJson(object);
	}
}
