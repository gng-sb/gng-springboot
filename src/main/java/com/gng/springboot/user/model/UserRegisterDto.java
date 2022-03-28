package com.gng.springboot.user.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gng.springboot.commons.constant.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Dto for register user
 * @author gchyoo
 *
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "userPwd")
@Component
public class UserRegisterDto {
	@Email(regexp = Constants.REGEXP_EMAIL, message = Constants.VALIDATE_USER_ID_EMAIL)
	private String userId;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Pattern(regexp = Constants.REGEXP_PW, message = Constants.VALIDATE_USER_PW_PATTERN)
	private String userPwd;

	@Size(min = 2, max = 20, message = Constants.VALIDATE_USER_NAME_SIZE)
	private String userName;
}
