package com.gng.springboot.commons.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StartEventHandler implements ApplicationListener<ApplicationStartedEvent> {
	@Value("${application.name}")
	private String applicationName;
	
	@Value("${application.version}")
	private String applicationVersion;
	
	public StartEventHandler() {
		
	}

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		log.info("=======================");
		log.info("{} v{} Start ...", applicationName, applicationVersion);
		log.info("=======================");
		log.info("Application Start Event");
		log.info("=======================");
	}
}
