package com.gng.springboot.commons.model;

import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class BaseDto {
	@Transient
	protected String accessToken;

	@Transient
	protected String refreshToken;
}
