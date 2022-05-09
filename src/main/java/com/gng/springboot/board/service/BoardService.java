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
	 
	public BoardEntity createBoard(BoardEntity boardEntity);
	
	public List<BoardEntity> searchAllBoard();
	
	public Optional<BoardEntity> searchBoard(Long id);
	
	public BoardEntity updateBoard(Long id, BoardEntity boardEntity);
	
	public void deleteBoard(Long id);
}


