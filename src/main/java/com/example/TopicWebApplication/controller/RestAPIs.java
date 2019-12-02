package com.example.TopicWebApplication.controller;

import java.awt.TextArea;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PreDestroy;
import javax.validation.Valid;

import org.hibernate.type.LocalDateTimeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.TopicWebApplication.model.Account;
import com.example.TopicWebApplication.model.Comment;
import com.example.TopicWebApplication.model.Role;
import com.example.TopicWebApplication.model.RoleName;
import com.example.TopicWebApplication.model.Topic;
import com.example.TopicWebApplication.model.TrueComment;
import com.example.TopicWebApplication.repository.AccountRepository;
import com.example.TopicWebApplication.repository.CommentRepository;
import com.example.TopicWebApplication.repository.RoleRepository;
import com.example.TopicWebApplication.repository.TopicRepository;
import com.example.TopicWebApplication.services.TopicServices;

import lombok.NonNull;

@RestController
@RequestMapping("/api")
public class RestAPIs {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	TopicRepository topicRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	TopicServices topicServices;
	
	@GetMapping("/test/user")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public String userAccess() {
		return ">>> User Contents!";
	}
	
	@GetMapping("/test/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return ">>> Admin Contents";
	}
	
	@GetMapping("/get-user-info")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<Account> getAccount(@RequestParam String username) {
		
		Account accountInfo = accountRepository.findByUsername(username);
		return ResponseEntity.ok().body(accountInfo);
	}
	
	@PutMapping("/update-account")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<Account> updateAccount(@NonNull @RequestBody Account account) {
		
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

	@GetMapping("/get-topic")
	public List<Topic> getTopicMainPage2(@RequestParam(defaultValue = "0") int pageNumber, 
			@RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(defaultValue = "creationDay") String sortBy) {
		
		List<Topic> temList = topicServices.getAllTopic(pageNumber, pageSize, sortBy);
		List<Topic> results = new ArrayList<Topic>();
		for (Topic topic : temList) {
			if (topic.getState() != 5) {
				results.add(topic);
			}
		}
		
		return results;
	}
	
	@GetMapping("/search-topic-by-key")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<Topic> getTopicByKeyWord(@RequestParam String key) {
		
		return null;
	}
	
	@GetMapping("/my-topics")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<Topic> getTopicForUser(@RequestParam String username) {

		Account account = accountRepository.findByUsername(username);
		List<Topic> result = topicRepository.findTopicByCreatedBy(account.getAccountId());
		
		return result;
	}
	
	@GetMapping("/get-account-by-admin")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Account> getAccountByAdmin() {
		return accountRepository.findAll();
	} 
	
	@PutMapping("/change-role-user-by-admin")
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
	
	@PutMapping("/activate-account")
	@PreAuthorize("hasRole('ADMIN')")
	public Account activateAccount(@RequestBody Account a) {
		
		Account updateActiveAccount = accountRepository.findByUsername(a.getUsername());
		
		updateActiveAccount.setActive(!a.getActive());
		return accountRepository.save(updateActiveAccount);
	}
	
	
	@PostMapping("/create-topic")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Topic createTopic(@RequestParam String title, @RequestParam String content, @RequestParam String username) {
		
		LocalDateTime dateTime = LocalDateTime.now();
		
		Account user = accountRepository.findByUsername(username);
		
		Topic newTopic = new Topic(title, content, dateTime, user.getAccountId());
		
		return topicRepository.save(newTopic);
	}
	
	@GetMapping("/is-true-owner")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public boolean isTrueOwner(@RequestParam String username, @RequestParam String id) {
		
		Account user = accountRepository.findByUsername(username);
		
		Optional<Topic> topic = topicRepository.findById(Long.valueOf(id));
		if (topic.get().getCreatedBy().equals(user.getAccountId())) {
			return true;
		} else {
			return false;
		}
	}
	
	@PostMapping("/submit-comment")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Topic submitComment(@RequestParam String id, @RequestParam String comment, @RequestParam String username) {
		LocalDateTime dateTime = LocalDateTime.now();
		
		Account user = accountRepository.findByUsername(username);
		
		Comment c = new Comment(comment, dateTime, user.getAccountId(), 3);
		commentRepository.save(c);
		
		Optional<Topic> t = topicRepository.findById(Long.parseLong(id));
		Set<Comment> sc = t.get().getCommentId();
		sc.add(c);
		
		return topicRepository.save(t.get());
	}
	
	@GetMapping("/get-list-is-true-comment")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<Comment> getListTrueComments(@RequestParam String username, @RequestParam String id) {
		List<Comment> listResult = new ArrayList<Comment>();
		
		Account user = accountRepository.findByUsername(username);
		
		Optional<Topic> topic = topicRepository.findById(Long.valueOf(id));
		Set<Comment> set = topic.get().getCommentId();
			
		if (userHasRoleAdmin(username)) {
			for (Comment comment : set) {
				comment.setState(1);
				listResult.add(comment);
			}
			return listResult;
		} else {
			for (Comment comment : set) {
				if (comment.getCreated_by() == user.getAccountId()) {
					comment.setState(1);
				} else {
					comment.setState(0);
				}
				
				listResult.add(comment);
			}
		}		
		
		return listResult;
	}
	
	@PutMapping("/update-topic")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Topic updateTopic(@RequestBody Topic topic) {
		
		return topicRepository.save(topic);
	}
	
	@PutMapping("/update-comment")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Comment updateComment(@RequestBody Comment comment) {
		
		return commentRepository.save(comment);
	}
	
	@PostMapping("/unlock-or-lock-topic")
	@PreAuthorize("hasRole('ADMIN')")
	public Topic unlockAndLockTopic(@RequestParam String id) {
		Optional<Topic> topic = topicRepository.findById(Long.valueOf(id));
		if (topic.get().getState() != 5) {
			topic.get().setState(5);
		} else if (topic.get().getState() == 5) {
			topic.get().setState(0);
		}
		return topicRepository.save(topic.get());
	}
	
	@PutMapping("/unlock-or-lock-comment")
	@PreAuthorize("hasRole('ADMIN')")
	public Comment unlockAndLockComment(@RequestParam String id) {
		Optional<Comment> comment = commentRepository.findById(Long.valueOf(id));
		if (comment.get().getState() != 3) {
			comment.get().setState(3);
		} else if (comment.get().getState() == 3) {
			comment.get().setState(0);
		}
		return commentRepository.save(comment.get());
	}
	
	@GetMapping("/get-topic-for-admin")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Topic> getTopicForAdmin(@RequestParam String username) {
		if (userHasRoleAdmin(username) == true) {
			return topicRepository.findAll();
		}
		
		return null;
	}
	
	public Boolean userHasRoleAdmin(String username) {
		
		Account user = accountRepository.findByUsername(username);
		
		for (Role role : user.getRoles()) {
			if (RoleName.ROLE_ADMIN.equals(role.getRole())) {
				return true;
			} else {
				return false;
			}
		}
		
		return null;
	}
	
	@GetMapping("/get-comment-for-admin")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Comment> getCommentsForAdmin(@RequestParam String username) {
		if (userHasRoleAdmin(username) == true) {
			return commentRepository.findAll();
		}
		return null;
	}
	
	@DeleteMapping("/delete-comment")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteComment(@RequestParam String id) {
		Optional<Comment> comment = commentRepository.findById(Long.valueOf(id));
		commentRepository.delete(comment.get());
	}
	
}