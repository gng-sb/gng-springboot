package com.gng.springboot.email.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.exception.custom.BusinessException;
import com.gng.springboot.commons.exception.custom.NoRollbackBusinessException;
import com.gng.springboot.commons.property.EmailProperty;
import com.gng.springboot.email.model.EmailConfirmEntity;
import com.gng.springboot.email.model.EmailMessage;
import com.gng.springboot.email.repository.EmailConfirmRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailConfirmServiceImpl implements EmailConfirmService {
	private final EmailConfirmRepository emailConfirmRepository;
	private final JavaMailSender javaMailSender;
	private final EmailProperty emailProperty;
	
	/**
	 * Send email confirmation token
	 * @param id
	 */
	@Transactional(rollbackFor = BusinessException.class, noRollbackFor = NoRollbackBusinessException.class)
	public void sendEmailConfirmToken(String id) {
		EmailConfirmEntity emailConfirmEntity = EmailConfirmEntity.createEmailConfirmToken(id, emailProperty.getToken().getValidTime());
		emailConfirmEntity = emailConfirmRepository.save(emailConfirmEntity);
		
		EmailMessage emailMessage = EmailMessage.builder()
				.to(id)
				.subject("회원가입 이메일 인증[GNGSB]")
				.text(String.format("http://%s:%s/gng/auth/email-confirm/%s", emailProperty.getHost(), emailProperty.getPort(), emailConfirmEntity.getUuid()))
				.build();
		
		sendEmail(emailMessage);
	}
	
	/**
	 * Send email
	 * @param simpleMailMessage
	 */
	@Async
	private void sendEmail(EmailMessage emailMessage) {
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
	 * @param uuid
	 */
	@Transactional(rollbackFor = BusinessException.class, noRollbackFor = NoRollbackBusinessException.class)
	public String confirmEmail(String uuid) {
		// Retrieve token data
		EmailConfirmEntity emailConfirmEntity = findByUuidAndExpiredAtAfterAndExpired(uuid);
		
		// Set token to used, account status to use
		emailConfirmEntity.useToken();
		
		// Update token, account
		emailConfirmRepository.save(emailConfirmEntity);
		
		return emailConfirmEntity.getId();
	}
	
	/**
	 * Retrieve valid email confirmation token
	 * @param uuid
	 * @return EmailConfirmEntity
	 */
	public EmailConfirmEntity findByUuidAndExpiredAtAfterAndExpired(String uuid) {
		Optional<EmailConfirmEntity> emailConfirmEntityOptional = emailConfirmRepository.findByUuidAndExpiredAtAfterAndExpired(uuid, LocalDateTime.now(), false);
		
		// Confirmation token is expired or not exist
		if(!emailConfirmEntityOptional.isPresent()) {
			Optional<EmailConfirmEntity> emailConfirmEntityExpiredOptional = emailConfirmRepository.findByUuid(uuid);
			
			if(emailConfirmEntityExpiredOptional.isPresent()) {
				// Re-send confirmation mail if valid uuid is given
				sendEmailConfirmToken(emailConfirmEntityExpiredOptional.get().getId());
				
				throw new NoRollbackBusinessException(ResponseCode.EMAIL_TOKEN_NOT_FOUND_RESEND);
			} else {
				// Do not send confirmation mail if invalid uuid is given
				throw new BusinessException(ResponseCode.EMAIL_TOKEN_NOT_FOUND);
			}
		}
		
		return emailConfirmEntityOptional.get();
	}
	
}
