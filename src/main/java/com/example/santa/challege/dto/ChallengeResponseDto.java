package com.example.santa.challege.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChallengeResponseDto {

    private Long id;
    private String name;
    private String description;
    private String image;
    private Integer clearStandard;
}
