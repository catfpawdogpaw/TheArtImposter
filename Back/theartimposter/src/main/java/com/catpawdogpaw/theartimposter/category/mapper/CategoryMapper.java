package com.catpawdogpaw.theartimposter.category.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.catpawdogpaw.theartimposter.category.model.Category;

@Mapper
public interface CategoryMapper {
    Category getCategoryById(@Param("id") Long id);

    List<Category> getAllCategories();


    void insertCategory(Category category);

    void deleteCategory(@Param("id") Long id);
}
