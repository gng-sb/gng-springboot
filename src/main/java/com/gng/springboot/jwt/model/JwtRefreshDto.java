package com.gng.springboot.jwt.model;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

import com.gng.springboot.commons.constant.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * Dto for register account
 * @author gchyoo
 *
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"accessToken", "refreshToken"})
@Component
public class JwtRefreshDto {

	@NotBlank(message = Constants.VALIDATE_ACCOUNT_ID_BLANK)
	@ApiModelProperty(value = "로그인 ID")
	private String id;

	@NotBlank(message = Constants.JWT_ACCESS_TOKEN_EMPTY)
	@Transient
	@ApiModelProperty(value = "Access token")
	private String accessToken;

	@NotBlank(message = Constants.JWT_REFRESH_TOKEN_EMPTY)
	@Transient
	@ApiModelProperty(value = "Refresh token")
	private String refreshToken;
}
