package com.catpawdogpaw.theartimposter.category;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catpawdogpaw.theartimposter.category.mapper.CategoryMapper;
import com.catpawdogpaw.theartimposter.category.model.Category;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public Category getCategoryById(Long id) {
        return categoryMapper.getCategoryById(id);
    }

    public List<Category> getAllCategories() {
        return categoryMapper.getAllCategories();
    }

    public void insertCategory(Category category) {
        categoryMapper.insertCategory(category);
    }


    public void deleteCategory(Long id) {
        categoryMapper.deleteCategory(id);
    }
}
