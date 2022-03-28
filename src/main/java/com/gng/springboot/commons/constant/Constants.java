package com.gng.springboot.commons.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Enum/constants class
 * @author gchyoo
 *
 */
public class Constants {
	
	public static final String REGEXP_EMAIL = ".+[@].+[\\.].+";
	public static final String REGEXP_PW = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$"; // (영문, 특수문자, 숫자 포함 8 ~ 20자)

	public static final String VALIDATE_USER_ID_BLANK = "사용자 계정을 입력해주세요.";
	public static final String VALIDATE_USER_ID_EMAIL = "올바른 형식의 이메일이 아닙니다.";
	public static final String VALIDATE_USER_PW_BLANK = "비밀번호를 입력해주세요.";
	public static final String VALIDATE_USER_PW_PATTERN = "영문자, 숫자, 특수문자를 1개 이상 포함한 8자 ~ 20자의 비밀번호를 입력해주세요.";
	public static final String VALIDATE_USER_NAME_BLANK = "사용자 이름을 입력해주세요.";
	
	
	/**
	 * User role enum
	 * @author gchyoo
	 *
	 */
	@ToString
	@Getter
	@AllArgsConstructor
	public enum RoleTypes {
		ROLE_ADMIN("Admin", "/admin/**", "관리자"),
		ROLE_USER("User", "/user/**", "사용자");
		
		private String role;
		private String matcher;
		private String description;
	}

	/**
	 * User status enum
	 * @author gchyoo
	 *
	 */
	@ToString
	@Getter
	@AllArgsConstructor
	public enum UserStatusTypes {
		USE(1, "활성화"),
		NOT_USE(0, "비활성화"),
		BLOCKED(-1, "차단");
		
		private int status;
		private String description;
	}
	
}
