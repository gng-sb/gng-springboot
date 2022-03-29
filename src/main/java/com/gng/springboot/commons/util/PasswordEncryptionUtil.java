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
	 * @param accountPwd
	 * @return Encrypted password
	 */
	public String encrypt(String accountPwd) {
		return passwordEncoder.encode(accountPwd);
	}
	
	/**
	 * Check account password is valid
	 * @param currentAccountPwd Encrypted password in database
	 * @param compareAccountPwd password inserted by account(Not encrypted)
	 * @return true/false (valid/not valid)
	 */
	public boolean isValidAccountPwd(String currentAccountPwd, String compareAccountPwd) {
		return passwordEncoder.matches(compareAccountPwd, currentAccountPwd);
	}
	
}
