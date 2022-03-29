package com.gng.springboot.commons.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Email properties
 * @author gchyoo
 *
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "mail")
@Component
public class EmailProperty {
	private String host;
	private String port;
	
	private Token token;
	
	@Getter
	@Setter
	@ToString
	public static class Token {
		private Long validTime;
	}
}
