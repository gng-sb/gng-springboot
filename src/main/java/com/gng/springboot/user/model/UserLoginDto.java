package com.gng.springboot.user.model;

import javax.persistence.Transient;

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
	
	@Transient
	private String jwt;
	
	private UserLoginDto(UserEntity userEntity, String jwt) {
		BeanUtils.copyProperties(userEntity, this);
		this.jwt = jwt;
	}
	
	public static UserLoginDto of(UserEntity userEntity, String jwt) {
		return new UserLoginDto(userEntity, jwt);
	}
}
