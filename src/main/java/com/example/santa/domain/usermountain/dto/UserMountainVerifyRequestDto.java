package com.example.santa.domain.usermountain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMountainVerifyRequestDto {

//    private String userEmail;
//    private Long categoryId;
    private LocalDate climbDate;
    private double latitude;
    private double longitude;
}
