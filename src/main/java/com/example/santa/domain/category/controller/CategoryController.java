package com.example.santa.domain.category.controller;

import com.example.santa.domain.category.dto.CreateDto;
import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @Valid CreateDto createDto) {
        String name = createDto.getName();
        Category category = categoryService.createCategory(name);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody @Valid CreateDto createDto) {
        String name = createDto.getName();
        Category category = categoryService.updateCategory(categoryId, name);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
