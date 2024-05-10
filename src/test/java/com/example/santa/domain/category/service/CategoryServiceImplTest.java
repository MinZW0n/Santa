package com.example.santa.domain.category.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.category.repository.CategoryRepository;
import com.example.santa.global.exception.ServiceLogicException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("등산");

    }

    @Test
    void createCategory_Success() {
        when(categoryRepository.findByName("등산")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.createCategory("등산");
        assertNotNull(result);
        assertEquals("등산", result.getName());
    }

    @Test
    void createCategory_ThrowsExceptionIfCategoryExists() {
        when(categoryRepository.findByName("등산")).thenReturn(Optional.of(category));

        Exception exception = assertThrows(ServiceLogicException.class, () ->
                categoryService.createCategory("등산")
        );
        assertEquals("이미 존재하는 카테고리입니다.", exception.getMessage());
    }

    @Test
    void getAllCategories_Success() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));

        List<Category> result = categoryService.getAllCategories();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("등산", result.get(0).getName());
    }

    @Test
    void updateCategory_Success() {
        lenient().when(categoryRepository.findByName("등산")).thenReturn(Optional.empty());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.updateCategory(1L, "산등");
        assertNotNull(result);
        assertEquals("산등", result.getName());
    }

    @Test
    void updateCategory_ThrowsExceptionIfNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ServiceLogicException.class, () ->
                categoryService.updateCategory(1L, "등산")
        );
        assertEquals("존재하지 않는 카테고리입니다.", exception.getMessage());
    }

    @Test
    void deleteCategory_Success() {
        categoryService.deleteCategory(1L);
        verify(categoryRepository).deleteById(1L);
    }
}
