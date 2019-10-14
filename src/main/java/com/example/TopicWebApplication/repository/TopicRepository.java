package com.example.TopicWebApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TopicWebApplication.model.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

}
