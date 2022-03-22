package com.gng.springboot.commons.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class Constants {
	
	/**
	 * 사용자 권한 enum
	 * @author gchyoo
	 *
	 */
	@ToString
	@Getter
	@AllArgsConstructor
	public enum UserRoles {
		ADMIN("Admin", "/admin/**", "관리자"),
		USER("User", "/user/**", "사용자");
		
		private String role;
		private String matcher;
		private String description;
	}

	/**
	 * 사용자 상태 enum
	 * @author gchyoo
	 *
	 */
	@ToString
	@Getter
	@AllArgsConstructor
	public enum UserStatus {
		USE(1, "활성화"),
		NOT_USE(0, "비활성화"),
		BLOCKED(-1, "차단");
		
		private int status;
		private String description;
	}
	
}
