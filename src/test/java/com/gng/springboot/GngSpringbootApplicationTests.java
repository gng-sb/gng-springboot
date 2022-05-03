package com.gng.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@ActiveProfiles(profiles = "test")
@SpringBootTest
class GngSpringbootApplicationTests {

	@Test
	void contextLoads() {
	}

}
