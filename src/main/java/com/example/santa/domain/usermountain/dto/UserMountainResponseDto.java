package com.example.santa.domain.usermountain.dto;

import com.example.santa.domain.mountain.dto.UserClimbMountainResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

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

    @Schema(description = "산 정보 DTO")
    private UserClimbMountainResponseDto mountain;
}
