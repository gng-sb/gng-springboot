package com.gng.springboot.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gng.springboot.jwt.model.AccountRefreshEntity;

/**
 * gng_accounts repository
 * @author gchyoo
 *
 */
public interface AccountRefreshRepository extends JpaRepository<AccountRefreshEntity, Long>{
	public Optional<AccountRefreshEntity> findByGngAccountIdAndUuid(Long gngAccountId, String uuid);
}
