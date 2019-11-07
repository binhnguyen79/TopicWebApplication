package com.example.TopicWebApplication.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.TopicWebApplication.model.Topic;


import java.util.List;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>, PagingAndSortingRepository<Topic, Long> {
	
	List<Topic> findByTitle(String title);
	List<Topic> findByCreatedBy(Long createdby);
	List<Topic> findByCreationDay(Date creationday);
	
	@Query("SELECT t FROM Topic t ORDER BY creation_day DESC")
	List<Topic> findAll();
	
	@Query("SELECT t FROM Topic t WHERE created_by = :createdBy ORDER BY creation_day DESC")
	List<Topic> findTopics(Long createdBy);
	
//	@Query("SELECT t FROM topic t WHERE title REGEXP :title")
//	List<Topic> searchTopics(String title);
	
	List<Topic> findAllByTitle(Pageable pageable);
}
