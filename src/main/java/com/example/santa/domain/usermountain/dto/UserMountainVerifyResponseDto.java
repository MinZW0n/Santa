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
public class UserMountainVerifyResponseDto {
    private Long id;
    private Long userId;
    private String mountainName;
    private String mountainLocation;
    private Double userAccumulatedHeight;
    private LocalDate climbDate;
    private String location;
    private Double latitude;
    private Double longitude;
}
