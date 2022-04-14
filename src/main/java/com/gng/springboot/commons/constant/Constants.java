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
	public static final String REGEXP_PW = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"; // (영문, 특수문자, 숫자 포함 8 ~ 20자)

	public static final String VALIDATE_ACCOUNT_ID_BLANK = "사용자 계정을 입력해주세요.";
	public static final String VALIDATE_ACCOUNT_ID_EMAIL = "올바른 형식의 이메일이 아닙니다.";
	public static final String VALIDATE_ACCOUNT_PW_BLANK = "비밀번호를 입력해주세요.";
	public static final String VALIDATE_ACCOUNT_PW_PATTERN = "영문자, 숫자, 특수문자를 1개 이상 포함한 8자 ~ 20자 사이의 비밀번호를 입력해주세요.";
	public static final String VALIDATE_ACCOUNT_NAME_BLANK = "사용자 이름을 입력해주세요.";
	public static final String VALIDATE_ACCOUNT_NAME_SIZE = "2자 ~ 20자 사이의 사용자 이름을 입력해주세요.";

	public static final String JWT_ACCESS_TOKEN_EMPTY = "Access token을 입력해주세요.";
	public static final String JWT_REFRESH_TOKEN_EMPTY = "Access token을 입력해주세요.";
	
	
	/**
	 * User role enum
	 * @author gchyoo
	 *
	 */
	@ToString
	@Getter
	@AllArgsConstructor
	public enum RoleTypes {
		ROLE_ADMIN("ADMIN", "관리자"),
		ROLE_USER("USER", "사용자");
		
		private String role;
		private String description;
	}

	/**
	 * Page matcher enum
	 * @author gchyoo
	 *
	 */
	@ToString
	@Getter
	@AllArgsConstructor
	public enum MatcherTypes {
		ACCOUNT("/account/**", "계정 페이지"),
		JWT("/jwt/**", "JWT 페이지"),
		BOARD("/board/**", "게시판 페이지");
		
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
	public enum AccountStatusTypes {
		USE(1, "활성화"),
		NOT_AUTHORIZED(0, "비활성화"),
		BLOCKED(-1, "정지");
		
		private int status;
		private String description;
		
		public static AccountStatusTypes getAccountStatusType(int accountStatus) {
			for(AccountStatusTypes accountStatusType : AccountStatusTypes.values()) {
				if(accountStatus == accountStatusType.getStatus()) {
					return accountStatusType;
				}
			}
			
			return null;
		}
	}
	
}
