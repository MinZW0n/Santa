package com.example.santa.domain.mountain.service;


import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;

import java.time.LocalDate;

public interface MountainService {

    UserMountainResponseDto verifyAndCreateUserMountain(double latitude, double longitude, LocalDate climbDate, String email);
    void updateProgress(String email, Long userMountainId);

}
