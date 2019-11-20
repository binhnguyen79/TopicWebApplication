package com.example.TopicWebApplication.model;

import java.awt.TextArea;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TemporalType;
import javax.print.attribute.standard.DateTimeAtCreation;

import org.dom4j.Text;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.repository.Temporal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "topic")
public class Topic {

	@Id
	@Column(name = "id_topic")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTopic;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "content")
	private String content;

	@Column(name = "creation_day")
	private LocalDateTime creationDay;
	
	@Column(name = "created_by")
	private Long createdBy;
	
	@Column(name = "state")
	@ColumnDefault(value = "1")
	private int state;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@OrderBy(value = "creation_day DESC")
	@JoinTable(name = "comment_topic", joinColumns = @JoinColumn(referencedColumnName = "id_topic"
		), inverseJoinColumns = @JoinColumn(referencedColumnName = "id_comment"))
	private Set<Comment> commentId;
	
	public Topic(String title, String content, LocalDateTime creationDay, long createdBy) {
		super();
		this.title = title;
		this.content = content;
		this.creationDay = creationDay;
		this.createdBy = createdBy;
	}
	
}
