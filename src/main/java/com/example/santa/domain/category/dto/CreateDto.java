package com.example.santa.domain.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {
    @Schema(description = "테스트 값", example = "등산")
    @NotBlank(message = "카테고리를 입력하세요.")
    private String name;
}
