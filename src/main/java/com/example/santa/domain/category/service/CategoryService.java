package com.example.santa.domain.category.service;

import com.example.santa.domain.category.entity.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(String name);

    List<Category> getAllCategories();

    Category updateCategory(Long id, String name);

    void deleteCategory(Long id);
}
