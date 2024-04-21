package com.example.santa.domain.category.service;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.category.repository.CategoryRepository;
import com.example.santa.global.exception.ExceptionCode;
import com.example.santa.global.exception.ServiceLogicException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name) {
        // 이름으로 카테고리 검색
        Optional<Category> existingCategory = categoryRepository.findByName(name);
        if (existingCategory.isPresent()) {
            // 중복된 이름이 있으면 예외 발생
            throw new ServiceLogicException(ExceptionCode.CATEGORY_ALREADY_EXISTS);
        }
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(Long id, String name) {
        // 이름으로 카테고리 검색
        Optional<Category> existingCategory = categoryRepository.findByName(name);
        if (existingCategory.isPresent()) {
            // 중복된 이름이 있으면 예외 발생
            throw new ServiceLogicException(ExceptionCode.CATEGORY_ALREADY_EXISTS);
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
        category.setName(name);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }


}
