package com.gng.springboot.account.model;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gng.springboot.commons.constant.Constants;

import io.swagger.annotations.ApiParam;
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
@ToString(exclude = "accountPwd")
@Component
public class AccountLoginDto {
	@ApiParam(value = "로그인 ID")
	@Email(regexp = Constants.REGEXP_EMAIL, message = Constants.VALIDATE_ACCOUNT_ID_EMAIL)
	private String accountId;
	
	@ApiParam(value = "로그인 PW")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message = Constants.VALIDATE_ACCOUNT_PW_BLANK)
	private String accountPwd;
	
	@Transient
	private String jwt;
	
	private AccountLoginDto(AccountEntity accountEntity, String jwt) {
		BeanUtils.copyProperties(accountEntity, this);
		this.jwt = jwt;
	}
	
	public static AccountLoginDto of(AccountEntity accountEntity, String jwt) {
		return new AccountLoginDto(accountEntity, jwt);
	}
}
