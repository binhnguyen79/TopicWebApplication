package com.example.TopicWebApplication.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import javax.validation.Valid;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.TopicWebApplication.model.Account;
import com.example.TopicWebApplication.model.Role;
import com.example.TopicWebApplication.model.RoleName;
import com.example.TopicWebApplication.model.Topic;
import com.example.TopicWebApplication.repository.AccountRepository;
import com.example.TopicWebApplication.repository.RoleRepository;
import com.example.TopicWebApplication.repository.TopicRepository;
import com.example.TopicWebApplication.services.TopicServices;

@RestController
public class RestAPIs {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	TopicRepository topicRepository;
	
	@Autowired
	TopicServices topicServices;
	
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
	
	public List<Topic> getTopicMainPage() {
		
		return topicRepository.findAll();
	}

	@GetMapping("/api/get-topic")
	public List<Topic> getTopicMainPage2(@RequestParam(defaultValue = "0") int pageNumber, 
			@RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(defaultValue = "creationDay") String sortBy) {
		
		List<Topic> results = topicServices.getAllTopic(pageNumber, pageSize, sortBy);
		
		return results;
	}
	
	@GetMapping("/api/search-topic-by-key")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<Topic> getTopicByKeyWord(@RequestParam String key) {
		
		return null;
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
	
	@PutMapping("/api/change-role-user-by-admin")
	@PreAuthorize("hasRole('ADMIN')")
	public Account changeRoleByAdmin(@RequestBody Account a) {
		
		Account updateAdminRoleAccount = accountRepository.findByUsername(a.getUsername());
		
		if (updateAdminRoleAccount.getRoles().size() <= 1) {
			Set<Role> adminRoles = updateAdminRoleAccount.getRoles();
			Optional<Role> admin = roleRepository.findByRole(RoleName.ROLE_ADMIN);
			Role adminRole = admin.get();
			adminRoles.add(adminRole);
			updateAdminRoleAccount.setRoles(adminRoles);
		} 
		else if (updateAdminRoleAccount.getRoles().size() >= 2) {
			Set<Role> adminRoles = new HashSet<>();
			Optional<Role> admin = roleRepository.findByRole(RoleName.ROLE_USER);
			Role adminRole = admin.get();
			adminRoles.add(adminRole);
			updateAdminRoleAccount.setRoles(adminRoles);
		}
		
		return accountRepository.save(updateAdminRoleAccount);
	}
	
	@PutMapping("/api/activate-account")
	@PreAuthorize("hasRole('ADMIN')")
	public Account activateAccount(@RequestBody Account a) {
		
		System.out.println(a.toString());
		Account updateActiveAccount = accountRepository.findByUsername(a.getUsername());
		
		updateActiveAccount.setActive(!a.getActive());
		return accountRepository.save(updateActiveAccount);
	}
	
}