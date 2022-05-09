package com.gng.springboot.board.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gng.springboot.board.model.BoardEntity;
import com.gng.springboot.board.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

/**
 * Board service implements
 * @author "hyunmo.gu"
 *
 */

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {
	private final BoardRepository boardRepository;
	
	@Override
	public BoardEntity createBoard(BoardEntity boardEntity) {
		
		
		return boardRepository.save(boardEntity);
	}

	@Override
	public List<BoardEntity> searchAllBoard() {
		return boardRepository.findAll();
	}

	@Override
	public Optional<BoardEntity> searchBoard(Long id) {
		
		return boardRepository.findById(id);
	}

	@Override
	public BoardEntity updateBoard(Long id, BoardEntity boardEntity) {
		
		boardEntity.setGngBoardId(id);
		return boardRepository.save(boardEntity);
	}

	@Override
	public void deleteBoard(Long id) {
		boardRepository.deleteById(id);
	}

}
