package com.gng.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class
 * @author gchyoo
 *
 */
@EnableAutoConfiguration
@SpringBootApplication
public class GngSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(GngSpringbootApplication.class, args);
	}

}
