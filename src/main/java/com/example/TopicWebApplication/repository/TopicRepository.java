package com.example.TopicWebApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TopicWebApplication.model.Topic;

import java.util.List;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
	List<Topic> findByTitle(String title);
	List<Topic> findByCreatedBy(Long createdby);
	List<Topic> findByCreationDay(Date creationday);
}
