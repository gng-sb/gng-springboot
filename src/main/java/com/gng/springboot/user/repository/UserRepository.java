package com.gng.springboot.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gng.springboot.user.model.UserEntity;

/**
 * gng_users repository
 * @author gchyoo
 *
 */
public interface UserRepository extends JpaRepository<UserEntity, Long>{
	public Optional<UserEntity> findByUserId(String userId);
}
