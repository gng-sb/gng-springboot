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

import io.swagger.annotations.ApiParam;
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

	@ApiParam(value = "로그인 ID")
	@Column(name = "account_id")
	private String accountId;

	@Id
	@ApiParam(value = "토큰 UUID")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "uuid")
	private String uuid;
	
	@ApiParam(value = "만료 여부")
	@Column(name = "expired", columnDefinition = "TINYINT")
	private boolean expired;
	
	@ApiParam(value = "만료 시각")
	@Column(name = "expired_at")
	private LocalDateTime expiredAt;
	
	/**
	 * Create email confirmation token
	 * @param accountId
	 * @return
	 */
	public static EmailConfirmEntity createEmailConfirmToken(String accountId, Long mailTokenValidTime) {
		EmailConfirmEntity emailConfirmEntity = new EmailConfirmEntity();
		
		emailConfirmEntity.expiredAt = LocalDateTime.now().plusMinutes(mailTokenValidTime); // 만료 시간
		emailConfirmEntity.accountId = accountId;
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
