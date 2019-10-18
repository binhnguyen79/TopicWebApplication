package com.example.TopicWebApplication.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.TopicWebApplication.model.Account;
import com.example.TopicWebApplication.model.CustomUserDetails;
import com.example.TopicWebApplication.repository.AccountRepository;

@Service
public class UserServices implements UserDetailsService {

	@Autowired
	AccountRepository accountRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		//check exist account
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        return CustomUserDetails.build(account);
	}

}
