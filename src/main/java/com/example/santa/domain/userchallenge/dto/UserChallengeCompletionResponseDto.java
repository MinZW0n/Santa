package com.example.santa.domain.userchallenge.dto;

import com.example.santa.domain.challege.dto.ChallengeNameResponseDto;
import com.example.santa.domain.challege.entity.Challenge;
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

    private ChallengeNameResponseDto challenge;

}
