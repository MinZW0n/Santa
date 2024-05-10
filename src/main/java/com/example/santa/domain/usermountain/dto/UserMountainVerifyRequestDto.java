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
@Schema(description = "등산 인증 요청 DTO")
public class UserMountainVerifyRequestDto {

    @Schema(description = "테스트 값", example = "2024-03-01")
    private LocalDate climbDate;

    @Schema(description = "테스트 값", example = "127.960749")
    private double latitude;

    @Schema(description = "테스트 값", example = "37.874149")
    private double longitude;
}
