package com.example.santa.domain.preferredcategory.dto;

import com.example.santa.domain.category.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoriesRequestDto {
    List<Long> categoryIds;
}
