package com.gng.springboot.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gng.springboot.account.model.AccountEntity;

/**
 * gng_accounts repository
 * @author gchyoo
 *
 */
public interface AccountRepository extends JpaRepository<AccountEntity, Long>{
	public Optional<AccountEntity> findById(String id);
	public void deleteById(String id);
}
