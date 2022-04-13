package com.gng.springboot.account.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gng.springboot.commons.constant.Constants;
import com.gng.springboot.commons.model.BaseDto;
import com.gng.springboot.jwt.component.JwtTokenProvider;

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
@ToString(exclude = "pwd")
@Component
public class AccountLoginDto extends BaseDto {
	@ApiParam(value = "로그인 ID")
	@Email(regexp = Constants.REGEXP_EMAIL, message = Constants.VALIDATE_ACCOUNT_ID_EMAIL)
	private String id;
	
	@ApiParam(value = "로그인 PW")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message = Constants.VALIDATE_ACCOUNT_PW_BLANK)
	private String pwd;
	
	@ApiParam(value = "이름")
	private String name;

	private AccountLoginDto(AccountEntity accountEntity, String accessToken, String refreshToken) {
		BeanUtils.copyProperties(accountEntity, this);
		super.accessToken = accessToken;
		super.refreshToken = refreshToken;
	}
	
	public static AccountLoginDto of(AccountEntity accountEntity, JwtTokenProvider jwtTokenProvider, String uuid) {
		String accessToken = jwtTokenProvider.createAccessToken(accountEntity.getId(), accountEntity.getRoleTypeSet());
		String refreshToken = jwtTokenProvider.createRefreshToken(uuid);
		
		return new AccountLoginDto(accountEntity, accessToken, refreshToken);
	}
}
