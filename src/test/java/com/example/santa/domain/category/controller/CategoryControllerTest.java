package com.example.santa.domain.category.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.category.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void createCategory_Success() throws Exception {
        // given
        String categoryName = "등산";
        Category category = new Category();
        category.setName(categoryName);
        given(categoryService.createCategory(anyString())).willReturn(category);

        // when & then
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + categoryName + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(categoryName));
    }

    @Test
    public void getAllCategories_Success() throws Exception {
        // given
        given(categoryService.getAllCategories()).willReturn(Arrays.asList(new Category(), new Category()));

        // when & then
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    public void updateCategory_Success() throws Exception {
        // given
        Long categoryId = 1L;
        String newCategoryName = "새 등산 카테고리";
        Category updatedCategory = new Category();
        updatedCategory.setName(newCategoryName);
        given(categoryService.updateCategory(eq(categoryId), anyString())).willReturn(updatedCategory);

        // when & then
        mockMvc.perform(patch("/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + newCategoryName + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newCategoryName));
    }

    @Test
    public void deleteCategory_Success() throws Exception {
        // given
        Long categoryId = 1L;
        willDoNothing().given(categoryService).deleteCategory(categoryId);

        // when & then
        mockMvc.perform(delete("/categories/" + categoryId))
                .andExpect(status().isOk());
    }
}
