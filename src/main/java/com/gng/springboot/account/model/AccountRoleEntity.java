package com.gng.springboot.account.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
 * gng_account_roles table entity
 * @author gchyoo
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Entity(name = "gng_account_roles")
@Table(name = "gng_account_roles")
public class AccountRoleEntity extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 3092653173143502698L;

	@Id
	@ApiParam(value = "gng_account_roles 테이블 ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gng_account_role_id")
	private Long gngAccountRoleId;
	
	@ApiParam(value = "gng_accounts 테이블 ID")
	@Column(name = "gng_account_id")
	private Long gngAccountId;
	
	@ApiParam(value = "사용자 권한")
	@Column(name = "role_type", columnDefinition = "VARCHAR")
	private RoleTypes roleType;
}
