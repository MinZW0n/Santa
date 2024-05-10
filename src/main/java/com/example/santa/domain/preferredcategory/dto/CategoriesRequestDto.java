package com.example.santa.domain.preferredcategory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesRequestDto {
    @Schema(description = "테스트 값", example = "1, 2, 3")
    List<Long> categoryIds;
}
