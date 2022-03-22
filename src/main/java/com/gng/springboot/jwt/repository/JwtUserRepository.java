package com.gng.springboot.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gng.springboot.jwt.model.GngUserEntity;

public interface JwtUserRepository extends JpaRepository<GngUserEntity, Long>{
	public Optional<GngUserEntity> findByUserId(String userId);
}
