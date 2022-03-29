package com.gng.springboot.board.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * gng_board table entity
 * @author "hyunmo.gu"
 *
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "gng_board")
public class BoardEntity {
	@Id
	@ApiParam(value = "gng_board 테이블 ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gng_board_id")
	private Long gngBoardId;
	
	@ApiParam(value = "게시글 제목")
	@Column(name = "gng_board_name")
	private String gngBoardName;
	
	@ApiParam(value = "게시글 내용")
	@Column(name = "gng_board_data")
	private String gngBoardData;
}
