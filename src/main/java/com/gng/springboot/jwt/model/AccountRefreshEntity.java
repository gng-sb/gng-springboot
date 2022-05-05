package com.gng.springboot.jwt.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gng.springboot.commons.model.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * gng_account_refresh_token table entity
 * @author gchyoo
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = {"uuid"})
@Entity(name = "gng_account_refresh_tokens")
@Table(name = "gng_account_refresh_tokens")
public class AccountRefreshEntity extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 6091588334190581807L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gng_account_refresh_token_id")
	private Long gngAccountRefreshTokenId;
	
	@Column(name = "gng_account_id")
	private Long gngAccountId;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "access_token", columnDefinition = "TEXT")
	private String accessToken;
	
	@Column(name = "refresh_token", columnDefinition = "TEXT")
	private String refreshToken;
}
