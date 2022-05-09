package com.gng.springboot.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

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
	
	@DisplayName("tdd 테스트")
	public class BoardCreateTest {
		@Test
		@DisplayName("create test")
		public void createBoardTest() throws Exception {
			// given
//			final BoardDto boardDto = 
		}
		
	}
}