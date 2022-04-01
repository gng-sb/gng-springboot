package com.gng.springboot.board.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gng.springboot.board.model.BoardEntity;
import com.gng.springboot.board.service.BoardService;

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
	private final BoardService boardService;
	
	@PostMapping("/create")
	public BoardEntity createBoard(@RequestBody BoardEntity boardEntity) {
		return boardService.createBoard(boardEntity);	
	}
	
	@GetMapping
	public List<BoardEntity> searchBoard(@RequestBody BoardEntity boardEntity) {
		return boardService.searchBoard();
//		return null;
	}
	
	@PostMapping("/update/{id}")
	public BoardEntity updateBoard(
			@PathVariable Long id, 
			@RequestBody BoardEntity boardEntity) {
		return boardService.updateBoard(id, boardEntity);
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteBoard(@PathVariable Long id) {
		boardService.deleteBoard(id);
	}
}
