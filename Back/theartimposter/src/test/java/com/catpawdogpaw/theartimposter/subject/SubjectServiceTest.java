package com.catpawdogpaw.theartimposter.subject;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.catpawdogpaw.theartimposter.category.CategoryService;
import com.catpawdogpaw.theartimposter.category.model.Category;
import com.catpawdogpaw.theartimposter.subject.model.Subject;


@SpringBootTest
public class SubjectServiceTest {
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SubjectService subjectService;
	
	@Test
    public void testInsertCategory() {
        Category category = new Category();
        category.setCategory("Test Category");

        categoryService.insertCategory(category);

        assertThat(category.getCategoryId()).isNotNull();
    }
	
	@Test
	public void testCreateSubject() {
        Subject subject = new Subject();
        subject.setSubject("Test Subject");
        subject.setCategoryId(1L);
        
        subjectService.createSubject(subject);

        assertThat(subject.getSubjectId()).isNotNull();
    }
	
	@Test
	public void createTestSubject() {
		 String[] categories = {
		            "Animals", "Food", "Sports", "Movies", "Music", 
		            "History", "Geography", "Science", "Literature", "Art", 
		            "Technology", "Nature", "Travel", "Mythology", "Fashion"
		        };

		        String[][] subjects = {
		            {"Dog", "Cat", "Elephant"},
		            {"Pizza", "Burger", "Sushi"},
		            {"Football", "Basketball", "Tennis"},
		            {"Inception", "Titanic", "The Matrix"},
		            {"Guitar", "Piano", "Violin"},
		            {"World War II", "Renaissance", "Industrial Revolution"},
		            {"Asia", "Africa", "Europe"},
		            {"Gravity", "Photosynthesis", "Relativity"},
		            {"Hamlet", "Pride and Prejudice", "1984"},
		            {"Mona Lisa", "Starry Night", "The Scream"},
		            {"Smartphone", "Internet", "AI"},
		            {"Rainforest", "Desert", "Ocean"},
		            {"Paris", "New York", "Tokyo"},
		            {"Greek Mythology", "Norse Mythology", "Egyptian Mythology"},
		            {"Haute Couture", "Streetwear", "Vintage"}
		        };

		        for (int i = 0; i < categories.length; i++) {
		            Category category = new Category( categories[i]);
		            categoryService.insertCategory(category);
		            
		            for (String subjectName : subjects[i]) {
		                Subject subject = new Subject();
		                subject.setSubject(subjectName);
		                subject.setCategoryId(category.getCategoryId());
		                subjectService.createSubject(subject);
		            }
		        }
	}
}
