package com.example.TopicWebApplication.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "comment")
public class Comment {

	@Id
	@Column(name = "id_comment")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id_comment;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "creation_day")
	private LocalDateTime creation_day;
	
	@Column(name = "created_by")
	private Long created_by;
	
	@Column(name = "state")
	private int state;
	
	public Comment(String comment, LocalDateTime dateTime, Long accountId, int i) {
		super();
		this.content = comment;
		this.creation_day = dateTime;
		this.created_by = accountId;
		this.state = i;
	}
}
