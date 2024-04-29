package com.example.santa.domain.user.service;

import com.example.santa.domain.user.dto.UserResponseDto;
import com.example.santa.domain.user.dto.UserSignInRequestDto;
import com.example.santa.domain.user.dto.UserSignupRequestDto;
import com.example.santa.domain.user.dto.UserUpdateRequestDto;
import com.example.santa.domain.user.entity.Password;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.global.security.jwt.JwtToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    // create
    Long signup(UserSignupRequestDto request);
    // duplicate
    Boolean checkEmailDuplicate(String email);
    Boolean checkNicknameDuplicate(String nickname);
    // login
    JwtToken signIn(UserSignInRequestDto userSignInRequestDto);
    // read
    UserResponseDto findUserByEmail(String email);

    // Users read(관리자)
    Page<UserResponseDto> findAllUser(Pageable pageable);

    UserResponseDto updateUser(String email, UserUpdateRequestDto userUpdateRequestDto);

    String changePassword(String email, String oldPassword, String newPassword);

    String resetPassword(String email, String newPassword);
//    Page<UserMountainResponseDto> findAllUserMountains(Pageable pageable);

    Page<UserMountainResponseDto> findAllUserMountains(String email, Pageable pageable);
}
