package com.catpawdogpaw.theartimposter.category;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catpawdogpaw.theartimposter.category.mapper.CategoryMapper;
import com.catpawdogpaw.theartimposter.category.model.Category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public Category getCategoryById(Long id) {
    	Category category = categoryMapper.getCategoryById(id);
    	log.info(category.getCategory() + " : " + "id :  "+ id + " categoryId"  + category.getCategoryId());
    	return category;
    	
    }

    public List<Category> getAllCategories() {
    	 List<Category> categories = categoryMapper.getAllCategories();
         System.out.println("Retrieved Categories: " + categories);
         return categories;
    }

    public void insertCategory(Category category) {
        categoryMapper.insertCategory(category);
        log.info(category.getCategory() + " : " +" categoryId"  + category.getCategoryId());
    }


    public void deleteCategory(Long id) {
        categoryMapper.deleteCategory(id);
    }
}
