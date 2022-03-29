package com.gng.springboot.email.service;

import java.time.LocalDateTime;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.custom.BusinessException;
import com.gng.springboot.commons.property.EmailProperty;
import com.gng.springboot.email.model.EmailConfirmEntity;
import com.gng.springboot.email.model.EmailMessage;
import com.gng.springboot.email.repository.EmailConfirmRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Service
public class EmailConfirmService {
	private final EmailConfirmRepository emailConfirmRepository;
	private final JavaMailSender javaMailSender;
	private final EmailProperty emailProperty;
	
	/**
	 * Send email confirmation token
	 * @param accountId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void sendEmailConfirmToken(String accountId) {
		EmailConfirmEntity emailConfirmEntity = EmailConfirmEntity.createEmailConfirmToken(accountId, emailProperty.getToken().getValidTime());
		emailConfirmEntity = emailConfirmRepository.save(emailConfirmEntity);
		
		EmailMessage emailMessage = EmailMessage.builder()
				.to(accountId)
				.subject("회원가입 이메일 인증[GNGSB]")
				// TODO: React 이메일 인증 주소로 변경하여야 함
				.text(String.format("http://%s:%s/account/email-confirm/%s", emailProperty.getHost(), emailProperty.getPort(), emailConfirmEntity.getUuid()))
				.build();
		
		sendEmail(emailMessage);
	}
	
	/**
	 * Send email
	 * @param simpleMailMessage
	 */
	@Async
	public void sendEmail(EmailMessage emailMessage) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(emailMessage.getTo());
			mimeMessageHelper.setSubject(emailMessage.getSubject());
			mimeMessageHelper.setText(emailMessage.getText());

			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new BusinessException(ResponseCode.EMAIL_TOKEN_SEND_FAILURE);
		}
	}

	/**
	 * Confirm email
	 * @param accountId
	 */
	@Transactional(rollbackFor = Exception.class)
	public String confirmEmail(String uuid) {
		// Retrieve token data
		EmailConfirmEntity emailConfirmEntity = findByUuidAndExpiredAtAfterAndExpired(uuid);
		
		// Set token to used, account status to use
		emailConfirmEntity.useToken();
		
		// Update token, account
		emailConfirmRepository.save(emailConfirmEntity);
		
		return emailConfirmEntity.getAccountId();
	}
	
	/**
	 * Retrieve valid email confirmation token
	 * @param uuid
	 * @return
	 */
	public EmailConfirmEntity findByUuidAndExpiredAtAfterAndExpired(String uuid) {
		return emailConfirmRepository.findByUuidAndExpiredAtAfterAndExpired(uuid, LocalDateTime.now(), false)
				.orElseThrow(() -> new BusinessException(ResponseCode.EMAIL_TOKEN_NOT_FOUND));
	}
	
}
