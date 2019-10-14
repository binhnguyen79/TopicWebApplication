package com.example.TopicWebApplication.controller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TopicWebApplication.jwt.JwtProvider;
import com.example.TopicWebApplication.message.request.LoginForm;
import com.example.TopicWebApplication.message.request.SignUpForm;
import com.example.TopicWebApplication.message.response.JwtResponse;
import com.example.TopicWebApplication.message.response.ResponseMessage;
import com.example.TopicWebApplication.model.Account;
import com.example.TopicWebApplication.model.CustomUserDetails;
import com.example.TopicWebApplication.model.Role;
import com.example.TopicWebApplication.model.RoleName;
import com.example.TopicWebApplication.repository.AccountRepository;
import com.example.TopicWebApplication.repository.RoleRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateAccount(@Valid @RequestBody LoginForm loginResquest) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginResquest.getUsername(), loginResquest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtProvider.generateJwtToken(authentication);
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		
		return ResponseEntity.ok(new JwtResponse(jwt, customUserDetails.getUsername(), customUserDetails.getAuthorities()));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerAccount(@Valid @RequestBody SignUpForm signUpForm) {
		if(accountRepository.existsByUsername(signUpForm.getUsername())) {
			return new ResponseEntity<>(new ResponseMessage("Username is already taken !!!"), HttpStatus.BAD_REQUEST);
		}
		
		if (accountRepository.existsByUsername(signUpForm.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("Email is already taken !!!"), HttpStatus.BAD_REQUEST);
		}
		
		//Creating account
		Account account = new Account(signUpForm.getUsername(), signUpForm.getName(), signUpForm.getEmail(), 
				passwordEncoder.encode(signUpForm.getPassword()));
		
		Set<String> strRoles = signUpForm.getRole();
		Set<Role> roles = new HashSet<>();
		
		strRoles.forEach(role -> {
			switch (role) {
			case "admin":
				Role adminRole = roleRepository.findByRole(RoleName.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
				roles.add(adminRole);

				break;
			default:
				Role userRole = roleRepository.findByRole(RoleName.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
				roles.add(userRole);
			}
		});
		
		account.setRoles(roles);
		accountRepository.save(account);
		
		return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
	}
}
