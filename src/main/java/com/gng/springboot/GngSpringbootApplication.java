package com.gng.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

/**
 * Main class
 * @author gchyoo
 *
 */
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableEncryptableProperties
@SpringBootApplication
public class GngSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(GngSpringbootApplication.class, args);
	}

}
