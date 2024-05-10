package com.example.santa.domain.rank.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "랭킹 응답 DTO")
public class RankingResponseDto {
    @Schema(description = "테스트 값", example = "1")
    private Long id;
    @Schema(description = "테스트 값", example = "1")
    private Long rank;
    @Schema(description = "테스트 값", example = "유저")
    private String nickname;
    private String image;
    @Schema(description = "테스트 값", example = "1000")
    private Integer score;
}
