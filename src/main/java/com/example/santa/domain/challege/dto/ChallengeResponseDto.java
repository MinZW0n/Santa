package com.example.santa.domain.challege.dto;

import com.example.santa.domain.category.dto.CreateDto;
import com.example.santa.domain.mountain.dto.UserClimbMountainResponseDto;
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
    private CreateDto category;
}
