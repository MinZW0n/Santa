package com.example.santa.domain.userchallenge.dto;

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
@Schema(description = "완료 된 챌린지 응답 DTO")
public class UserChallengeCompletionResponseDto {
    @Schema(description = "테스트 값", example = "2024-05-01")
    private LocalDate completionDate;
    @Schema(description = "테스트 값", example = "1")
    private Integer progress;
    @Schema(description = "테스트 값", example = "100대 명산 등반")
    private String challengeName;
    @Schema(description = "테스트 값", example = "100대 명산을 등반해보세요!")
    private String challengeDescription;
    private String challengeImage;
    @Schema(description = "테스트 값", example = "100")
    private Integer challengeClearStandard;
}
