package com.catpawdogpaw.theartimposter.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.catpawdogpaw.theartimposter.category.model.Category;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testInsertCategory() {
        Category category = new Category();
        category.setCategory("Test Category");

        categoryService.insertCategory(category);
        log.info("test insert category : " +  category.getCategoryId() + "  "+ category.getCategory());
        assertThat(category.getCategoryId()).isNotNull();
    }
    
    @Test
    public void testGetCategory() {
    	List<Category> allCategories = categoryService.getAllCategories();
    	
    	for(Category category : allCategories) {
    		assertThat(category.getCategoryId()).isNotNull();
    		 log.info("test get category : " + category.getCategoryId() + "  "+ category.getCategory());
    	}
    }
}
