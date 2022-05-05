package com.gng.springboot.email.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.gng.springboot.commons.model.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * gng_email_confirm table entity
 * @author gchyoo
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Entity(name = "gng_email_confirm")
@Table(name = "gng_email_confirm")
public class EmailConfirmEntity extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -585266495686927898L;

	@Column(name = "id")
	@ApiModelProperty(value = "로그인 ID")
	private String id;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "uuid")
	@ApiModelProperty(value = "토큰 UUID")
	private String uuid;
	
	@Column(name = "expired", columnDefinition = "TINYINT")
	@ApiModelProperty(value = "만료 여부")
	private boolean expired;
	
	@Column(name = "expired_at")
	@ApiModelProperty(value = "만료 시각")
	private LocalDateTime expiredAt;
	
	/**
	 * Create email confirmation token
	 * @param id
	 * @return
	 */
	public static EmailConfirmEntity createEmailConfirmToken(String id, Long mailTokenValidTime) {
		EmailConfirmEntity emailConfirmEntity = new EmailConfirmEntity();
		
		emailConfirmEntity.expiredAt = LocalDateTime.now().plusMinutes(mailTokenValidTime); // 만료 시간
		emailConfirmEntity.id = id;
		emailConfirmEntity.expired = false;
		
		return emailConfirmEntity;
	}
	
	/**
	 * Set token to used
	 */
	public void useToken() {
		this.expired = true;
	}
}
