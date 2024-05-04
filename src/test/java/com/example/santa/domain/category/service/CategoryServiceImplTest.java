//package com.example.santa.domain.category.service;
//
//import static org.mockito.Mockito.*;
//import static org.mockito.BDDMockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.example.santa.domain.category.entity.Category;
//import com.example.santa.domain.category.repository.CategoryRepository;
//import com.example.santa.global.exception.ServiceLogicException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.util.Optional;
//import java.util.Arrays;
//import java.util.List;
//
//@SpringBootTest
//class CategoryServiceImplTest {
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @InjectMocks
//    private CategoryServiceImpl categoryServiceImpl;
//
//    @BeforeEach
//    void setUp() {
//        categoryServiceImpl = new CategoryServiceImpl(categoryRepository);
//    }
//
//    @Test
//    void createCategory_Success() {
//        // given
//        String categoryName = "등산";
//        given(categoryRepository.findByName(categoryName)).willReturn(Optional.empty());
//        given(categoryRepository.save(any(Category.class))).willAnswer(invocation -> invocation.getArgument(0));
//
//        // when
//        Category createdCategory = categoryServiceImpl.createCategory(categoryName);
//
//        // then
//        assertNotNull(createdCategory);
//        assertEquals(categoryName, createdCategory.getName());
//    }
//
//    @Test
//    void createCategory_Fail_WhenCategoryAlreadyExists() {
//        // given
//        String categoryName = "등산";
//        given(categoryRepository.findByName(categoryName)).willReturn(Optional.of(new Category()));
//
//        // when & then
//        assertThrows(ServiceLogicException.class, () -> categoryServiceImpl.createCategory(categoryName));
//    }
//
//    @Test
//    void getAllCategories_Success() {
//        // given
//        given(categoryRepository.findAll()).willReturn(Arrays.asList(new Category(), new Category()));
//
//        // when
//        List<Category> categories = categoryServiceImpl.getAllCategories();
//
//        // then
//        assertNotNull(categories);
//        assertEquals(2, categories.size());
//    }
//
//    @Test
//    void updateCategory_Success() {
//        // given
//        Long categoryId = 1L;
//        String newCategoryName = "새 등산 카테고리";
//        Category category = new Category();
//        category.setId(categoryId);
//        category.setName("등산");
//        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));
//        given(categoryRepository.findByName(newCategoryName)).willReturn(Optional.empty());
//        given(categoryRepository.save(any(Category.class))).willAnswer(invocation -> invocation.getArgument(0));
//
//        // when
//        Category updatedCategory = categoryServiceImpl.updateCategory(categoryId, newCategoryName);
//
//        // then
//        assertNotNull(updatedCategory);
//        assertEquals(newCategoryName, updatedCategory.getName());
//    }
//
//    @Test
//    void deleteCategory_Success() {
//        // given
//        Long categoryId = 1L;
//        willDoNothing().given(categoryRepository).deleteById(categoryId);
//
//        // when
//        categoryServiceImpl.deleteCategory(categoryId);
//
//        // then
//        verify(categoryRepository, times(1)).deleteById(categoryId);
//    }
//}
