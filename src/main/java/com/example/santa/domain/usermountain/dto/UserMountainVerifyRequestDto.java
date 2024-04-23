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
public class UserMountainVerifyRequestDto {

    private Long userId;
    private Long categoryId;
    private Date climbDate;
    private double latitude;
    private double longitude;
}
