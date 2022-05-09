package com.gng.springboot.board.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gng.springboot.board.model.BoardDto;
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
	public BoardDto createBoard(BoardDto boardDto) {
		return boardRepository.save(boardDto);
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
	public BoardDto updateBoard(Long id, BoardDto boardDto) {
		
		boardDto.setGngBoardId(id);
		return boardRepository.save(boardDto);
	}

	@Override
	public void deleteBoard(Long id) {
		boardRepository.deleteById(id);
	}

}
