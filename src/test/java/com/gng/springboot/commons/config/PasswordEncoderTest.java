package com.gng.springboot.commons.config;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ulisesbocchio.jasyptspringboot.configuration.EnableEncryptablePropertiesConfiguration;

/**
 * PasswordEncoder encode/match test
 * @author gchyoo
 *
 */
@TestPropertySource(locations = "classpath:application.yml")
@Import(EnableEncryptablePropertiesConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PasswordEncoderTest {
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	@DisplayName("Password encryption test")
	public void encryptPassword() {
		String plainPassword = "passwordTest1234!";
		
		String encryptedPassword = passwordEncoder.encode(plainPassword);
		
		assertAll(
				() -> assertNotEquals(plainPassword, encryptedPassword),
				() -> assertTrue(passwordEncoder.matches(plainPassword, encryptedPassword))
		);
	}
}
