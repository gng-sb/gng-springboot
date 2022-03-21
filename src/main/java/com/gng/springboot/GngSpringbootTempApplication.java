package com.gng.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

/**
 * Main class
 * @author gchyoo
 *
 */
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableEncryptableProperties
@EnableJpaAuditing
@SpringBootApplication
public class GngSpringbootTempApplication {

	public static void main(String[] args) {
		SpringApplication.run(GngSpringbootTempApplication.class, args);
	}

}
