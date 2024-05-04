package com.example.santa.domain.usermountain.service;

import com.example.santa.domain.mountain.entity.Mountain;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.domain.usermountain.dto.UserMountainVerifyRequestDto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface UserMountainService {


//    UserMountainResponseDto verifyAndCreateUserMountain(double latitude, double longitude, LocalDate climbDate, String email );

    UserMountainResponseDto verifyAndCreateUserMountain(UserMountainVerifyRequestDto userMountainVerifyRequestDto,String email);

}
