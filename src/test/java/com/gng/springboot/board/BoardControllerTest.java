package com.gng.springboot.board;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

import com.gng.springboot.board.model.BoardDto;
import com.gng.springboot.board.repository.BoardRepository;
import com.gng.springboot.board.service.BoardService;
import com.gng.springboot.board.service.BoardServiceImpl;
import com.gng.springboot.commons.base.BaseControllerTest;

@DisplayName("BoardController 테스트")
public class BoardControllerTest extends BaseControllerTest{
	@Autowired
	private BoardService boardService;
	
	
	@Autowired
	private BoardRepository boardRepository;
	
	@MockBean
	BoardServiceImpl boardServiceImpl;
	
	private final static String id = "testId@test.com";
	private final static String name = "testName";
	private final static String data = "집가고싶다";
	
	@Nested
	@DisplayName("tdd 테스트")
	public class BoardCreateTest {
		
		@DisplayName("create test")
		@Test
		public void createBoardTest() throws Exception {
			// given
			BoardDto boardDto = BoardDto.builder()
					.gngAccountId(id)
					.boardName(name)
					.boardData(data)
					.build();
			
			// when
			mockMvc.perform(post("/board")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaTypes.HAL_JSON_VALUE)
					.content(objectMapper.writeValueAsString(boardDto)))
				.andDo(print())
				.andExpect(status().isCreated())
//				.andExpect(jsonPath("gngAccountId").exists())
				;
		}
		
	}
}