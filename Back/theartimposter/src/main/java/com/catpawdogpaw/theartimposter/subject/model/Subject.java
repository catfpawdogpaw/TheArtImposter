package com.catpawdogpaw.theartimposter.subject.model;

import com.catpawdogpaw.theartimposter.category.model.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Subject {
	private Long subjectId;
	private String subject;
	private	Category category;

}
