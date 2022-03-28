package com.gng.springboot.user.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gng.springboot.commons.constant.Constants;
import com.gng.springboot.commons.constant.Constants.RoleTypes;
import com.gng.springboot.commons.model.BaseEntity;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * gng_users table entity
 * @author gchyoo
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = "userPwd")
@Entity(name = "gng_users")
@Table(name = "gng_users")
public class UserEntity extends BaseEntity implements UserDetails, Serializable {
	
	private static final long serialVersionUID = 739282898877248753L;

	@Id
	@ApiParam(value = "gng_users 테이블 ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gng_user_id")
	private Long gngUserId;
	
	@ApiParam(value = "사용자 계정")
	@Email(regexp = Constants.REGEXP_EMAIL, message = Constants.VALIDATE_USER_ID_EMAIL)
	@NotBlank(message = Constants.VALIDATE_USER_ID_BLANK)
	@Column(name = "user_id")
	private String userId;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ApiParam(value = "사용자 비밀번호")
	@NotBlank(message = Constants.VALIDATE_USER_PW_BLANK)
	@Pattern(regexp = Constants.REGEXP_PW, message = Constants.VALIDATE_USER_PW_PATTERN)
	@Transient
	private String userPlainPwd;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ApiParam(value = "사용자 비밀번호")
	@Column(name = "user_pwd")
	private String userPwd;
	
	@ApiParam(value = "사용자 닉네임")
	@NotBlank(message = Constants.VALIDATE_USER_NAME_BLANK)
	@Size(min = 2, max = 20)
	@Column(name = "user_name")
	private String userName;
	
	@ApiParam(value = "계정 활성화 여부")
	@Column(name = "user_status", columnDefinition = "BIT", length=1)
	private int userStatus;

	@Builder.Default // Default value to new HashSet
	@ElementCollection(fetch = FetchType.EAGER) // Immediate loading
	@CollectionTable(
			name = "gng_user_roles", // Table name
			joinColumns = @JoinColumn(name = "gng_user_id", referencedColumnName = "gng_user_id") // Join column name
	)
	@Column(name = "role_type") // RoleTypes column name
	private Set<RoleTypes> roleTypeSet = new HashSet<>();
	
	public void addRoleType(RoleTypes roleType) {
		this.roleTypeSet.add(roleType);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roleTypeSet.stream()
				.map(roleTypes -> roleTypes.getRole())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}
	
	@Override
	public String getUsername() {
		return userId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getPassword() {
		return userPwd;
	}
}
