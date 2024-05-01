package com.example.santa.domain.mountain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "산 정보 DTO")
public class UserClimbMountainResponseDto {
    @Schema(description = "테스트 값", example = "가리산")
    private String name;
    @Schema(description = "테스트 값", example = "강원도 홍천군 두촌면 화촌면, 춘천시 북산면 동면")
    private String location;
    @Schema(description = "테스트 값", example = "1050.9")
    private Double height;
}
