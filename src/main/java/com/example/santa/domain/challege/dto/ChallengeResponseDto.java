package com.example.santa.domain.challege.dto;

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
    private String categoryName;
}
