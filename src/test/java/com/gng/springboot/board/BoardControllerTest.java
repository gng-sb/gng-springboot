package com.gng.springboot.board;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.gng.springboot.board.controller.BoardController;
import com.gng.springboot.board.model.BoardDto;
import com.gng.springboot.board.service.BoardServiceImpl;
import com.gng.springboot.commons.base.BaseControllerTest;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.handler.RestAPIExceptionHandler;

@DisplayName("BoardController 테스트")
public class BoardControllerTest extends BaseControllerTest{
	@Mock
	private BoardServiceImpl boardServiceImpl;
	
	private final static String id = "testId@test.com";
	private final static String name = "testName";
	private final static String data = "집가고싶다";
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new BoardController(boardServiceImpl))
				.setControllerAdvice(RestAPIExceptionHandler.class) // ControllerAdvice 클래스 추가
				.addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.toString(), true)) // UTF-8 필터 추가
				.build();
	}
	
	@Nested
	@DisplayName("tdd 테스트")
	public class BoardCreateTest {
		
		@DisplayName("create test")
		@Test
		public void createBoardTest() throws Exception {
			// given
			BoardDto boardDto = BoardDto.builder()
					.gngBoardId(1L)
					.gngAccountId(id)
					.boardName(name)
					.boardData(data)
					.build();

			BDDMockito.given(boardServiceImpl.createBoard(boardDto))
					.willReturn(boardDto);			
			// when
			mockMvc.perform(post("/board")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaTypes.HAL_JSON_VALUE)
					.content(toJson(boardDto)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.messages").value(ResponseCode.BOARD_CREATE_SUCCESS.getMessage()))
				.andExpect(jsonPath("$.result.gngBoardId").exists())
				;
		}
		
	}
}