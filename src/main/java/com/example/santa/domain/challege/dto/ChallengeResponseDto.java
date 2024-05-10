package com.example.santa.domain.challege.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "챌린지 응답 DTO")
public class ChallengeResponseDto {

    @Schema(description = "테스트 값", example = "1")
    private Long id;
    @Schema(description = "테스트 값", example = "힐링 모임 5회 참여")
    private String name;
    @Schema(description = "테스트 값", example = "힐링 모임에 5회 참여해보세요!")
    private String description;
    private String image;
    @Schema(description = "테스트 값", example = "5")
    private Integer clearStandard;
    @Schema(description = "테스트 값", example = "힐링")
    private String categoryName;
}
