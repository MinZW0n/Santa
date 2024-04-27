package com.example.santa.domain.usermountain.dto;

import com.example.santa.domain.mountain.dto.UserClimbMountainResponseDto;
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
public class UserMountainResponseDto {
    private Long id; //유저 마운틴 id
    private LocalDate climbDate;
    private UserClimbMountainResponseDto mountain;
}
