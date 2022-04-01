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
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@Entity(name = "gng_boards")
@Table(name = "gng_boards")
public class BoardEntity {
   @Id
   @ApiParam(value = "gng_board 테이블 ID")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "gng_board_id")
   private Long gngBoardId;
   
   @ApiParam(value = "로그인 ID")
   @Column(name = "user_id")
   private String userId;
   
   @ApiParam(value = "게시글 제목")
   @Column(name = "board_name")
   private String boardName;
   
   @ApiParam(value = "게시글 내용")
   @Column(name = "board_data", columnDefinition = "LONGTEXT")
   private String boardData;
}