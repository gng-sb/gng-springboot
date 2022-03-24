package com.gng.springboot.user.model;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Dto for login user
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
public class UserLoginDto {
	private String userId;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String userPwd;
	
	private UserLoginDto(UserEntity userEntity) {
		BeanUtils.copyProperties(userEntity, this);
	}
	
	public static UserLoginDto of(UserEntity userEntity) {
		return new UserLoginDto(userEntity);
	}
}
