package com.gng.springboot.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gng.springboot.board.model.BoardEntity;
import com.gng.springboot.board.reposigtory.BoardRepository;

import lombok.RequiredArgsConstructor;

/**
 * Board service
 * @author "hyunmo.gu"
 *
 */
@RequiredArgsConstructor
@Service
public class BoardService {
	private final BoardRepository boardRepository;
	 
	public BoardEntity createBoard(BoardEntity boardEntity) {
		return boardRepository.save(boardEntity);
	}
	
	public List<BoardEntity> searchBoard() {
		return boardRepository.findAll();
	}
	
	public BoardEntity updateBoard(Long id, BoardEntity boardEntity) {
		boardEntity.setGngBoardId(id);
		return boardRepository.save(boardEntity);
	}
	
	public void deleteBoard(Long id) {
		boardRepository.deleteById(id);
	}
}


