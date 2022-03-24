package com.gng.springboot.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gng.springboot.commons.model.BaseEntity;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * gng_users / gng_user_roles table entity
 * @author enemf
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
public class UserEntity extends BaseEntity implements UserDetails , Serializable{
	
	private static final long serialVersionUID = 739282898877248753L;

	@Id
	@ApiParam(value = "gng_users 테이블 ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gng_user_id")
	private Long gngUserId;
	
	@ApiParam(value = "사용자 계정")
	@Column(name = "user_id")
	private String userId;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ApiParam(value = "사용자 비밀번호")
	@Column(name = "user_pwd")
	private String userPwd;
	
	@ApiParam(value = "사용자 닉네임")
	@Column(name = "user_name")
	private String userName;
	
	@ApiParam(value = "계정 활성화 여부")
	@Column(name = "user_status", columnDefinition = "BIT", length=1)
	private int userStatus;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
			name = "gng_user_roles",
			joinColumns=@JoinColumn(name = "gng_user_id", referencedColumnName = "gng_user_id")
	)
	@Column(name = "user_roles", columnDefinition = "TEXT")
	@Builder.Default
	private List<String> userRoles = new ArrayList<>();
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.userRoles.stream()
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
