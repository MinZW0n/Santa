package com.example.santa.domain.usermountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMountainResponseDto {
    private Long id;
    private Long userId;
    private Long mountainId;
    private Double latitude;
    private Double longitude;
}
