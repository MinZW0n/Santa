package com.example.santa.domain.userchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserChallengeCompletionResponseDto {
    private LocalDate completionDate;
    private Integer progress;
    private String challengeName;
    private String challengeDescription;
    private String challengeImage;
    private Integer challengeClearStandard;
}
