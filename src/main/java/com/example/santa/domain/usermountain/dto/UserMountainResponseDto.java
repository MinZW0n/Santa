package com.example.santa.domain.usermountain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "등산 인증 완료 응답 DTO")
public class UserMountainResponseDto {

    @Schema(description = "테스트 값", example = "1")
    private Long id; //유저 마운틴 id

    @Schema(description = "테스트 값", example = "2024-03-01")
    private LocalDate climbDate;

    @Schema(description = "테스트 값", example = "가리산")
    private String mountainName;
    @Schema(description = "테스트 값", example = "강원도 홍천군 두촌면 화촌면, 춘천시 북산면 동면")
    private String mountainLocation;
    @Schema(description = "테스트 값", example = "1050.9")
    private Double mountainHeight;
}
