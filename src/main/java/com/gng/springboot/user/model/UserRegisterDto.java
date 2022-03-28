package com.gng.springboot.user.model;

import javax.validation.constraints.NotBlank;

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
	private String userId;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String userPwd;
	
	@NotBlank(message = Constants.VALIDATE_USER_NAME_BLANK)
	private String userName;
}
