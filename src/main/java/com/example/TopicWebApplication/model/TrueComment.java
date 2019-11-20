package com.example.TopicWebApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrueComment {
	private Long idComment;
	private boolean isTrue;
}
