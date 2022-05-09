package com.gng.springboot.board.service;

import java.util.List;
import java.util.Optional;

import com.gng.springboot.board.model.BoardDto;
import com.gng.springboot.board.model.BoardEntity;

/**
 * Board service
 * @author "hyunmo.gu"
 *
 */

public interface BoardService {
	 
	public BoardDto createBoard(BoardDto boardDto);
	
	public List<BoardEntity> searchAllBoard();
	
	public Optional<BoardEntity> searchBoard(Long id);
	
	public BoardDto updateBoard(Long id, BoardDto boardDto);
	
	public void deleteBoard(Long id);
}


