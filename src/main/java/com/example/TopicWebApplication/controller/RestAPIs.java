package com.example.TopicWebApplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.TopicWebApplication.model.Account;
import com.example.TopicWebApplication.model.Topic;
import com.example.TopicWebApplication.repository.AccountRepository;
import com.example.TopicWebApplication.repository.TopicRepository;

@RestController
public class RestAPIs {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	TopicRepository topicRepository;
	
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
	
	@GetMapping("/api/get-user-info")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<Account> getAccount(@RequestParam String username) {
		
		Account accountInfo = accountRepository.findByUsername(username);
		return ResponseEntity.ok().body(accountInfo);
	}
	
	@PutMapping("/api/update-account")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
		
		Account updateAccount = accountRepository.findByUsername(account.getUsername());
		
		updateAccount.setName(account.getName());
		updateAccount.setEmail(account.getEmail());
		if(account.getPassword() != null) {
			updateAccount.setPassword(account.getPassword());
		}
		
		accountRepository.save(updateAccount);
		
		return ResponseEntity.ok().body(updateAccount);
	}
	
	@GetMapping("/api/get-topic")
	public List<Topic> getTopicMainPage() {
		
		return topicRepository.findAll();
	}
	
	@GetMapping("/api/get-topic-by-key")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<Topic> getTopicByKeyWord(@RequestParam String key) {
		System.out.println(topicRepository.findByTitle(key).toString());
		return topicRepository.findByTitle(key);
	}
	
	@SuppressWarnings("null")
	@GetMapping("/api/my-topics")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<Topic> getTopicForUser(@RequestParam String username) {

		Account account = accountRepository.findByUsername(username);
		List<Topic> tempList = topicRepository.findTopics(account.getAccountId());
		List<Topic> result = null;
		
		for (int i = 0; i < tempList.size(); i++) {
			if(tempList.get(i).getCreatedBy() == account.getAccountId()) {
				 result.add(tempList.get(i));
			}
		}
		
		return result;
	}
	
	@GetMapping("/api/get-account-by-admin")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Account> getAccountByAdmin() {
		return accountRepository.findAll();
	} 
	
	@PutMapping("/api/activate-account")
	@PreAuthorize("hasRole('ADMIN')")
	public Account activateAccount(@RequestParam Account account) {
		return accountRepository.save(account);
	}
	
	@PutMapping("/api/update-account-by-admin")
	@PreAuthorize("hasRole('ADMIN')")
	public Account updateAccountByAdmin(@RequestParam Account account) {
		return accountRepository.save(account);
	}
	
	@DeleteMapping("/api/delete-account")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteAccountByAdmin(@RequestParam String username) {
		accountRepository.deleteByUsername(username);
	}
	
}