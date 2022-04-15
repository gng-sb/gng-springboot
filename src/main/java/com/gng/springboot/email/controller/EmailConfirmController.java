package com.gng.springboot.email.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gng.springboot.account.service.AccountService;
import com.gng.springboot.commons.constant.ResponseCode;
import com.gng.springboot.commons.model.ResponseDto;
import com.gng.springboot.email.service.EmailConfirmService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * Email controller
 * - /account/email-confirm/{uuid}
 * </pre>
 * @author gchyoo
 *
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/account")
@RestController
public class EmailConfirmController {
	
	private final EmailConfirmService emailConfirmService;
	private final AccountService accountService;
	
	@ApiOperation(
			value = "인증 메일 확인",
			notes = "인증 메일 전송 시 발급된 uuid 값을 통해 이메일 인증을 완료한다."
			)
	@PostMapping("/email-confirm/{uuid}")
	public ResponseEntity<ResponseDto<String>> confirmEmail(
			@ApiParam(name = "uuid") @PathVariable(required = true) String uuid
			) {
		log.info("Confirm email [uuid={}]", uuid);
		
		String id = emailConfirmService.confirmEmail(uuid);
		accountService.accountConfirm(id);

		ResponseDto<String> responseDto = new ResponseDto<>(ResponseCode.EMAIL_TOKEN_CONFIRM_SUCCESS, id);
		
		return ResponseEntity.status(responseDto.getHttpStatus())
				.body(responseDto);
	}
}
