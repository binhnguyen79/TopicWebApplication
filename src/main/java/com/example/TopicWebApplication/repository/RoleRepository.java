package com.example.TopicWebApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TopicWebApplication.model.Role;
import com.example.TopicWebApplication.model.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRole(RoleName role);
}
