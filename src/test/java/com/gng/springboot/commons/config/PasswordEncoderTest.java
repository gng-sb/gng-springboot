package com.gng.springboot.commons.config;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import com.ulisesbocchio.jasyptspringboot.configuration.EnableEncryptablePropertiesConfiguration;

/**
 * PasswordEncoder encode/match test
 * @author gchyoo
 *
 */
@TestPropertySource(locations = "classpath:application.yml") // 설정 파일 import
@Import(value = {EnableEncryptablePropertiesConfiguration.class}) // 필요한 클래스 Import
@ExtendWith(MockitoExtension.class) // Mockito
@DisplayName("PasswordEncoder 비밀번호 암호화 테스트")
public class PasswordEncoderTest {
	@Spy
	private BCryptPasswordEncoder passwordEncoder;
	
	@Test
	@DisplayName("성공")
	public void success() {
		// Given
		String plainPassword = "passwordTest1234!";
		
		// When
		String encryptedPassword = passwordEncoder.encode(plainPassword);
		
		// Then
		assertAll(
				() -> assertNotEquals(plainPassword, encryptedPassword),
				() -> assertTrue(passwordEncoder.matches(plainPassword, encryptedPassword))
		);
	}
}
