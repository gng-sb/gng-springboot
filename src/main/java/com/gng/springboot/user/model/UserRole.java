package com.gng.springboot.user.model;

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
 * gng_user_roles table entity
 * @author gchyoo
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Entity(name = "gng_user_roles")
@Table(name = "gng_user_roles")
public class UserRole extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 3092653173143502698L;

	@Id
	@ApiParam(value = "gng_user_roles 테이블 ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gng_user_role_id")
	private Long gngUserRoleId;
	
	@ApiParam(value = "gng_users 테이블 ID")
	@Column(name = "gng_user_id")
	private Long gngUserId;
	
	@ApiParam(value = "사용자 권한")
	@Column(name = "role_type", columnDefinition = "VARCHAR")
	private RoleTypes roleType;
}
