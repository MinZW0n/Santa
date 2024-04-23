package com.example.santa.domain.usermountain.service;

import com.example.santa.domain.mountain.entity.Mountain;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;

import java.util.Date;
import java.util.List;

public interface UserMountainService {

    Mountain test(double latitude, double longitude);

    UserMountainResponseDto verifyAndCreateUserMountain(double latitude, double longitude, Date climbDate, String userEmail, Long categoryId);

    List<UserMountainResponseDto> getAllUserMountains();

    UserMountainResponseDto getUserMountainById(Long id);

}
