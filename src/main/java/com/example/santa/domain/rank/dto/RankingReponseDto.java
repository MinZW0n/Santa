package com.example.santa.domain.rank.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RankingReponseDto {

    private Long id;
    private Long rank;
    private String nickname;
    private String image;
    private Integer score;
}
