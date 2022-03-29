package com.gng.springboot.email.controller;

import org.springframework.web.bind.annotation.RestController;

import com.gng.springboot.email.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * Email controller
 * </pre>
 * @author gchyoo
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class EmailController {
	
	private final EmailService emailService;
	
}
