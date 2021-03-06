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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gng_board_id")
	private Long gngBoardId;
	
	@Column(name = "gng_account_id")
	private String gngAccountId;
	
	@Column(name = "board_name")
	private String boardName;
	
	@Column(name = "board_data", columnDefinition = "LONGTEXT")
	private String boardData;
	
//	@ApiModelProperty(value = "μμ± μΌμ")
//	@Column(name = "created_at", columnDefinition = "DATETIME")
//	@DateTimeFormat(pattern = "yy-dd-MM")
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-dd-MM")
//	private String createTime;
}