package com.gng.springboot.board.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gng.springboot.board.model.BoardEntity;
import com.gng.springboot.board.repository.BoardRepository;

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
	
	public List<BoardEntity> searchAllBoard() {
		return boardRepository.findAll();
	}
	
	public Optional<BoardEntity> searchBoard(Long id) {
		return boardRepository.findById(id);
	}
	
	public BoardEntity updateBoard(Long id, BoardEntity boardEntity) {
		boardEntity.setGngBoardId(id);
		return boardRepository.save(boardEntity);
	}
	
	public void deleteBoard(Long id) {
		boardRepository.deleteById(id);
	}
}


