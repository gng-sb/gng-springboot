package com.gng.springboot.commons.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CloseEventHandler implements ApplicationListener<ContextClosedEvent>{
	@Value("${application.name}")
	private String applicationName;
	
	@Value("${application.version}")
	private String applicationVersion;
	
	public CloseEventHandler() {
		
	}
	
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		
		log.info("=======================");
		log.info("{} v{} Close ...", applicationName, applicationVersion);
		log.info("=======================");
		log.info("Application Close Event");
		log.info("=======================");
	}
	

}
