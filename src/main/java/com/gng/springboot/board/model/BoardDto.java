package com.gng.springboot.board.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "gng_boards")
@Table(name = "gng_boards")
public class BoardDto {
	@ApiModelProperty(value = "gng_board 테이블 ID")
	private Long gngBoardId;
	
	@ApiModelProperty(value = "로그인 ID")
	private String gngAccountId;
	
	@ApiModelProperty(value = "게시글 제목")
	private String boardName;
	
	@ApiModelProperty(value = "게시글 내용")
	private String boardData;
}
