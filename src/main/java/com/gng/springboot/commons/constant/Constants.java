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
