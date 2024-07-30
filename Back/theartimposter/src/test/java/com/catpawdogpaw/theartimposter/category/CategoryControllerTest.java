package com.catpawdogpaw.theartimposter.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.catpawdogpaw.theartimposter.category.model.Category;

@SpringBootTest
public class CategoryControllerTest {

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        categoryService.getAllCategories().forEach(category -> categoryService.deleteCategory(category.getCategoryId()));
    }

    @Test
    public void testInsertAndGetCategory() {
        Category category = new Category("과일과 채소");
        categoryService.insertCategory(category);
        Category found = categoryService.getCategoryById(category.getCategoryId());
        assertNotNull(found);
        assertEquals(category.getCategory(), found.getCategory());
    }

    @Test
    public void testGetAllCategories() {
        Category category1 = new Category("과일과 채소");
        Category category2 = new Category("동물");
        categoryService.insertCategory(category1);
        categoryService.insertCategory(category2);

        List<Category> categories = categoryService.getAllCategories();
        assertEquals(2, categories.size());
    }

    @Test
    public void testDeleteCategory() {
        Category category = new Category("과일과 채소");
        categoryService.insertCategory(category);
        categoryService.deleteCategory(category.getCategoryId());
        Category found = categoryService.getCategoryById(category.getCategoryId());
        assertNull(found);
    }
}
