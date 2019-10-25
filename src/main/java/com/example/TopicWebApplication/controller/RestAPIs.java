package com.example.TopicWebApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.TopicWebApplication.model.Account;
import com.example.TopicWebApplication.repository.AccountRepository;

@RestController
@CrossOrigin(origins = "*")
public class RestAPIs {
	
	@Autowired
	AccountRepository accountRepository;
	
	@GetMapping("/api/test/user")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public String userAccess() {
		return ">>> User Contents!";
	}
	
	@GetMapping("/api/test/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return ">>> Admin Contents";
	}
	
	@GetMapping("/api/user")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<Account> getAccount(@RequestParam String username) {
		
		Account accountInfo = accountRepository.findByUsername(username);
		return ResponseEntity.ok().body(accountInfo);
	}
}