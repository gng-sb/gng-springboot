package com.gng.springboot.commons.config;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ulisesbocchio.jasyptspringboot.configuration.EnableEncryptablePropertiesConfiguration;

/**
 * PasswordEncoder encode/match test
 * @author gchyoo
 *
 */
@TestPropertySource(locations = "classpath:application.yml") // 설정 파일 import
@Import(EnableEncryptablePropertiesConfiguration.class) // 필요한 클래스 Import
@ExtendWith(SpringExtension.class) // JUnit 5
@SpringBootTest // @SpringBootApplication 클래스에서 context를 찾음
public class PasswordEncoderTest {
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	@DisplayName("비밀번호 암호화 테스트")
	public void encryptPassword() {
		String plainPassword = "passwordTest1234!";
		
		String encryptedPassword = passwordEncoder.encode(plainPassword);
		
		assertAll(
				() -> assertNotEquals(plainPassword, encryptedPassword),
				() -> assertTrue(passwordEncoder.matches(plainPassword, encryptedPassword))
		);
	}
}
