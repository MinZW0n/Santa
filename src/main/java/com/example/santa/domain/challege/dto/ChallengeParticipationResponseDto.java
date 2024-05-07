package com.example.santa.domain.challege.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeParticipationResponseDto {

    private String challengeName;
    private long userCount;


}
