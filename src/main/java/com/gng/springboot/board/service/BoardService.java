package com.gng.springboot.board.service;

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
	
//	public PracticeEntity createClub(PracticeEntity practiceEntity) {
//		return practiceRepository.save(practiceEntity);
//	}
}


