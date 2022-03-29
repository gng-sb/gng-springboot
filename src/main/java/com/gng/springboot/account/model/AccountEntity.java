package com.gng.springboot.account.model;

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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
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
 * gng_accounts table entity
 * @author gchyoo
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = "accountPwd")
@Entity(name = "gng_accounts")
@Table(name = "gng_accounts")
public class AccountEntity extends BaseEntity implements UserDetails, Serializable {
	
	private static final long serialVersionUID = 739282898877248753L;

	@Id
	@ApiParam(value = "gng_accounts 테이블 ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gng_account_id")
	private Long gngAccountId;
	
	@ApiParam(value = "사용자 계정")
	@Column(name = "account_id")
	private String accountId;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ApiParam(value = "사용자 비밀번호")
	@Column(name = "account_pwd")
	private String accountPwd;
	
	@ApiParam(value = "사용자 닉네임")
	@Column(name = "account_name")
	private String accountName;
	
	@ApiParam(value = "계정 활성화 여부")
	@Column(name = "account_status", columnDefinition = "BIT", length=1)
	private int accountStatus;

	@Builder.Default // Default value to new HashSet
	@ElementCollection(fetch = FetchType.EAGER) // Immediate loading
	@CollectionTable(
			name = "gng_account_roles", // Table name
			joinColumns = @JoinColumn(name = "gng_account_id", referencedColumnName = "gng_account_id") // Join column name
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
		return accountId;
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
		return accountPwd;
	}
}
