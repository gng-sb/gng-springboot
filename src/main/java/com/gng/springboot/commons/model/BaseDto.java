package com.gng.springboot.commons.model;

import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"accessToken", "refreshToken"})
@Component
public class BaseDto {
	@Transient
	protected String accessToken;

	@Transient
	protected String refreshToken;
}
