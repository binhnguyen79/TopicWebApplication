package com.example.TopicWebApplication.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.TopicWebApplication.model.Account;
import com.example.TopicWebApplication.model.Comment;
import com.example.TopicWebApplication.model.Role;
import com.example.TopicWebApplication.model.RoleName;
import com.example.TopicWebApplication.model.Topic;
import com.example.TopicWebApplication.repository.AccountRepository;
import com.example.TopicWebApplication.repository.TopicRepository;

@Service
public class TopicServices {
	
	@Autowired
	TopicRepository topicRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	public List<Topic> getAllTopic(int pageNumber, int pageSize, String sortBy) {
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());	
		
		Page<Topic> pageResults = topicRepository.findAll(pageable);
		
		if (pageResults.hasContent()) {
			return pageResults.getContent();
		} else {
			return new ArrayList<Topic>();
		}
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
	
//	public Set<Comment> sortByDateListComment(Set<Comment> set) {
//		
//		TreeSet<Comment> tSet = new TreeSet<Comment>(new CreationDateInComparator());
//		for (Comment comment : set) {
//			tSet.add(comment);
//		}
//		
//		return tSet;
//	}
//	
//	class CreationDateInComparator implements Comparator<Comment> {
//		@Override
//		public int compare(Comment o1, Comment o2) {
//			return o2.getCreation_day().compareTo(o1.getCreation_day());
//		}
//	}
}
