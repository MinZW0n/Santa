package com.example.santa.domain.challege.dto;

import com.example.santa.domain.challege.entity.Challenge;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeNameResponseDto {
    //Dto네이밍 변경하기
    private String name;
    private String description;
    private String image;
    private Integer clearStandard;

}
