package com.example.santa.domain.mountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserClimbMountainResponseDto {
    private String name;
    private String location;
    private Double height;
}
