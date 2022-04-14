package com.gng.springboot.commons.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Separate @EnableJpaAuditing from application class
 * @author gchyoo
 *
 */
@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig {

}
