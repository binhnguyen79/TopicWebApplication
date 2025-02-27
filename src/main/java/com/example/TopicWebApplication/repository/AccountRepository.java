package com.example.TopicWebApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TopicWebApplication.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	public default Boolean isTrueActiveByUsername(String username) {
		return findByUsername(username).getActive();
	}
	
	Account findByUsername(String username);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
	Optional<Account> findByEmail(String email);
	
	void deleteByUsername(String username);
	
}
