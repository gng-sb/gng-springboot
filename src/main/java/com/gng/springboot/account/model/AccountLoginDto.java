package com.gng.springboot.account.model;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gng.springboot.commons.constant.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Dto for login account
 * @author gchyoo
 *
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "pwd")
@Component
public class AccountLoginDto {
	@Email(regexp = Constants.REGEXP_EMAIL, message = Constants.VALIDATE_ACCOUNT_ID_EMAIL)
	@ApiModelProperty(value = "로그인 ID")
	private String id;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message = Constants.VALIDATE_ACCOUNT_PW_BLANK)
	@ApiModelProperty(value = "로그인 PW")
	private String pwd;
	
	@ApiModelProperty(value = "이름")
	private String name;

	@Transient
	@ApiModelProperty(value = "Access token")
	protected String accessToken;

	@Transient
	@ApiModelProperty(value = "Refresh token")
	protected String refreshToken;
	
	private AccountLoginDto(AccountEntity accountEntity, String accessToken, String refreshToken) {
		BeanUtils.copyProperties(accountEntity, this);
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
	
	public static AccountLoginDto of(AccountEntity accountEntity, String accessToken, String refreshToken) {
		return new AccountLoginDto(accountEntity, accessToken, refreshToken);
	}
}
