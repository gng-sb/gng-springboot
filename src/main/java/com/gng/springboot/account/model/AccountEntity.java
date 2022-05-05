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
import com.gng.springboot.commons.constant.Constants.AccountStatusTypes;
import com.gng.springboot.commons.constant.Constants.RoleTypes;
import com.gng.springboot.commons.model.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@ToString(exclude = "pwd")
@EqualsAndHashCode(callSuper = false, of = {"gngAccountId"})
@Entity(name = "gng_accounts")
@Table(name = "gng_accounts")
public class AccountEntity extends BaseEntity implements UserDetails, Serializable {
	
	private static final long serialVersionUID = 739282898877248753L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gng_account_id")
	private Long gngAccountId;
	
	@Column(name = "id")
	private String id;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "pwd")
	private String pwd;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "status", columnDefinition = "BIT", length=1)
	private int status;

	@Builder.Default // Default value to new HashSet
	@ElementCollection(fetch = FetchType.EAGER) // Immediate loading
	@CollectionTable(
			name = "gng_account_roles", // Table name
			joinColumns = @JoinColumn(name = "gng_account_id", referencedColumnName = "gng_account_id") // Join column name
	)
	@Column(name = "role_type") // RoleTypes column name
	private Set<String> roleTypeSet = new HashSet<>();
	
	public void addRoleType(RoleTypes roleType) {
		this.roleTypeSet.add(roleType.name());
	}
	
	public AccountStatusTypes getAccountStatus() {
		return AccountStatusTypes.getAccountStatusType(this.status);
	}
	
	public void setAccountStatus(AccountStatusTypes accountStatusType) {
		this.status = accountStatusType.getStatus();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roleTypeSet.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}
	
	@Override
	public String getUsername() {
		return id;
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
		return pwd;
	}
}
