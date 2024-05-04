package com.example.santa.domain.category.controller;

import com.example.santa.domain.category.dto.CreateDto;
import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.category.service.CategoryService;
import com.example.santa.domain.challege.dto.ChallengeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "카테고리 생성 기능(관리자)", description = "카테고리 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Category.class)))
           })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody @Valid CreateDto createDto) {
        String name = createDto.getName();
        Category category = categoryService.createCategory(name);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @Operation(summary = "카테고리 조회 기능", description = "카테고리 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Category.class)))
    })
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @Operation(summary = "카테고리 수정 기능(관리자)", description = "카테고리 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Category.class)))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long categoryId, @RequestBody @Valid CreateDto createDto) {
        String name = createDto.getName();
        Category category = categoryService.updateCategory(categoryId, name);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @Operation(summary = "카테고리 삭제 기능(관리자)", description = "카테고리 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Category.class)))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
