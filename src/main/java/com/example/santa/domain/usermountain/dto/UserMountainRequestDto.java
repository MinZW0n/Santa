package com.example.santa.domain.usermountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMountainRequestDto {
    private Long userId;
    private Long mountainId;
    private Long categoryId;
    private double latitude;
    private double longitude;
}
