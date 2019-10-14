package com.example.TopicWebApplication.model;

import java.sql.Date;
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "topic")
public class Topic {

	@Id
	@Column(name = "id_topic")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idTopic;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "creation_day")
	private Date creationDay;
	
	@Column(name = "created_by")
	private Long createdBy;
	
	@Column(name = "state")
	private int state;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "comment_topic", joinColumns = @JoinColumn(referencedColumnName = "topicid_topic"
		), inverseJoinColumns = @JoinColumn(referencedColumnName = "commentid_comment"))
	private Set<Comment> comment;
}
