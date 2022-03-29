package com.gng.springboot.email.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class EmailMessage {
	private String to;
	private String subject;
	private String text;
}