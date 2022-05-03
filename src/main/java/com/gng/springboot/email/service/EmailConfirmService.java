package com.gng.springboot.email.service;

import com.gng.springboot.email.model.EmailConfirmEntity;

public interface EmailConfirmService {
	public void sendEmailConfirmToken(String id);
	public String confirmEmail(String uuid);
	public EmailConfirmEntity findByUuidAndExpiredAtAfterAndExpired(String uuid);
}
