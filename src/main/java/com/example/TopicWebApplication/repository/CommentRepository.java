package com.example.TopicWebApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TopicWebApplication.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
