package com.example.santa.domain.preferredcategory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreferredCategoryRequestDto {
    @Schema(description = "테스트 값", example = "1")
    private Long CategoryId;
}
