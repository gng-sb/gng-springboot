package com.gng.springboot.board.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gng.springboot.board.model.BoardDto;
import com.gng.springboot.board.model.BoardEntity;
import com.gng.springboot.board.service.BoardServiceImpl;
import com.gng.springboot.commons.model.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Board controller
 * 
 * - /board
 * - /boardNew
 * @author hyunmo.gu
 *
 */

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
@RestController
public class BoardController {
	private final BoardServiceImpl boardServiceImpl;
	// 페이징 개수 - 20개 단위
	// 페이징 https://devlog-wjdrbs96.tistory.com/414
	// selectBoard(Pageable pageable)							(BODY 없음) 페이지 최초 가져오기	: GET /board
	// selectBoard(Pageable pageable)							(BODY 없음) 페이지 가져오기		: GET /board?page={num}
	// selectArticle(@PathParam(required=true) id)				(BODY 없음) 게시글 가져오기		: GET /board/{id}
	// createArticle(@RequestBody(required=true) boardEntity)	(BODY 있음) 게시글 생성			: POST /board
	// updateArticle(@RequestBody(required=true) boardEntity)	(BODY 있음) 게시글 수정			: POST /board/{id} 
	// deleteArticle(@RequestBody(required=true) boardEntity)	(BODY 있음) 게시글 삭제			: DELETE /board/{id}

	
	@GetMapping("")
	public List<BoardEntity> searchAllBoard() {
		return boardServiceImpl.searchAllBoard();
	}
	
	@GetMapping("/{id}")
	public Optional<BoardEntity> searchBoard(@PathVariable Long id) {
		return boardServiceImpl.searchBoard(id);
	}
	
	@PostMapping(value = "", produces = {MediaTypes.HAL_JSON_VALUE})
	public ResponseEntity<ResponseDto<BoardDto>> createBoard2(
			@Valid @RequestBody(required = true) BoardDto boardDto
			){
		ResponseDto<BoardDto> responseDto = new ResponseDto<>(null, boardServiceImpl.createBoard(boardDto));
		
		return ResponseEntity.status(responseDto.getHttpStatus()).body(responseDto);
		
	}
	
//	@PostMapping("")
//	public BoardEntity createBoard(@RequestBody BoardDto boardDto) {
//		return boardServiceImpl.createBoard(boardDto);
//	}
	
	@PostMapping("/{id}")
	public BoardDto updateBoard(
			@PathVariable Long id, 
			@RequestBody BoardDto boardDto) {
		return boardServiceImpl.updateBoard(id, boardDto);
	}
	
	@DeleteMapping("/{id}")
	public void deleteBoard(@PathVariable Long id) {
		boardServiceImpl.deleteBoard(id);
	}
}
