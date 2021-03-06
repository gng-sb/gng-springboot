package com.gng.springboot.account.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
 * Dto for register account
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
public class AccountRegisterDto {
	@ApiModelProperty(value = "로그인 ID")
	@Email(regexp = Constants.REGEXP_EMAIL, message = Constants.VALIDATE_ACCOUNT_ID_EMAIL)
	private String id;

	@ApiModelProperty(value = "로그인 PW")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Pattern(regexp = Constants.REGEXP_PW, message = Constants.VALIDATE_ACCOUNT_PW_PATTERN)
	private String pwd;

	@ApiModelProperty(value = "이름")
	@Size(min = 2, max = 20, message = Constants.VALIDATE_ACCOUNT_NAME_SIZE)
	private String name;
}
