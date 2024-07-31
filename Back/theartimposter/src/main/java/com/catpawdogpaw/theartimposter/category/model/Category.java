package com.catpawdogpaw.theartimposter.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
	private Long categoryId;
	private String category;
	public Category(String category) {
        this.category = category;
    }
}
