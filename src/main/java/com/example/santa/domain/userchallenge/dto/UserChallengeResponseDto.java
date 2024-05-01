package com.example.santa.domain.userchallenge.dto;

import com.example.santa.domain.challege.dto.ChallengeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserChallengeResponseDto {
    private Long id;
    private Long user_id;
    private Long challenge_id;
    private String progress;
    private Boolean isCompleted;
    private Date completionDate;
    private ChallengeResponseDto challenge;

}
