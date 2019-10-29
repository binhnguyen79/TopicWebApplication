package com.example.TopicWebApplication.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "display_name"
            }),
            @UniqueConstraint(columnNames = {
                "email"
            })
        })
public class Account {
	
	@Id
	@Column(name = "id_account")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;
	
	@Column(name = "display_name")
	@NotEmpty(message = "Please provide your Username")
	@Length(max = 50)
	private String username;
	
	@Column(name = "fullname")
	private String name;
	
	@Column(name = "email")
	@NotEmpty(message = "Please provide your email")
	@Email(message = "Please provide valid email")
	private String email;
	
	@Column(name = "password")
	@JsonProperty(access = Access.WRITE_ONLY)
	@NotEmpty(message = "Please enter your password")
	@Length(min = 5, message = "Please enter password at least 8 characters")
	private String password;
	
	@Column(name = "active")
	private Boolean active;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "account_role", joinColumns = @JoinColumn(referencedColumnName = "id_account"), 
	inverseJoinColumns = @JoinColumn(referencedColumnName = "id_role"))
	private Set<Role> roles;

	public Account(@NotEmpty(message = "Please provide your Username") @Length(max = 50) String username, String name,
			@NotEmpty(message = "Please provide your email") @Email(message = "Please provide valid email") String email,
			@NotEmpty(message = "Please enter your password") @Length(min = 5, message = "Please enter password at least 8 characters") String password) {
		super();
		this.username = username;
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
}
