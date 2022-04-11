package com.gng.springboot.email.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gng.springboot.email.model.EmailConfirmEntity;

public interface EmailConfirmRepository extends JpaRepository<EmailConfirmEntity, Long> {

	public Optional<EmailConfirmEntity> findByUuid(String uuid);
	public Optional<EmailConfirmEntity> findByUuidAndExpiredAtAfterAndExpired(String uuid, LocalDateTime now, boolean b);
}
