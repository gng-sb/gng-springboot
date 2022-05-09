package com.gng.springboot.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gng.springboot.board.model.BoardDto;
import com.gng.springboot.board.model.BoardEntity;

/**
 * @author "hyunmo.gu"
 *
 */
public interface BoardRepository extends JpaRepository<BoardEntity,Long>{

	BoardDto save(BoardDto boardDto);
	
	
}
