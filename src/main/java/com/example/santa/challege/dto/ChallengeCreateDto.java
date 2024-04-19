package com.example.santa.challege.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeCreateDto {

    @NotNull(message = "업적 명은 필수 입력란 입니다.")
    private String name;

    @NotNull(message = "업적 설명은 필수 입력란 입니다.")
    private String description;

    @NotNull(message = "완료 기준은 필수 입력란 입니다.")
    @Min(value = 1, message = "1이상의 값을 입력해 주세요.")
    private Integer clearStandard;

    private String image;

//    private String progress;
}
