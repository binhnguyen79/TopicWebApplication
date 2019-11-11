package com.example.TopicWebApplication.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.TopicWebApplication.model.Topic;
import com.example.TopicWebApplication.repository.TopicRepository;

@Service
public class TopicServices {
	
	@Autowired
	TopicRepository topicRepository;
	
	public List<Topic> getAllTopic(int pageNumber, int pageSize, String sortBy) {
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());	
		
		Page<Topic> pageResults = topicRepository.findAll(pageable);
		
		if (pageResults.hasContent()) {
			return pageResults.getContent();
		} else {
			return new ArrayList<Topic>();
		}
	}
}
