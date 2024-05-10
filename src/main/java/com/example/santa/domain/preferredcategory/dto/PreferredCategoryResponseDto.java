package com.example.santa.domain.preferredcategory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferredCategoryResponseDto {
    @Schema(description = "테스트 값", example = "힐링")
    private String categoryName;
}
