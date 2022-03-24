package com.gng.springboot.commons.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Password encryption class
 * @author gchyoo
 *
 */
@RequiredArgsConstructor
@Component
public class PasswordEncryptionUtil {
	
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * Encrypt password
	 * @param userPwd
	 * @return Encrypted password
	 */
	public String encrypt(String userPwd) {
		return passwordEncoder.encode(userPwd);
	}
	
	/**
	 * Check user password is valid
	 * @param currentUserPwd Encrypted password in database
	 * @param compareUserPwd password inserted by user(Not encrypted)
	 * @return true/false (valid/not valid)
	 */
	public boolean isValidUserPwd(String currentUserPwd, String compareUserPwd) {
		return passwordEncoder.matches(compareUserPwd, currentUserPwd);
	}
	
}
